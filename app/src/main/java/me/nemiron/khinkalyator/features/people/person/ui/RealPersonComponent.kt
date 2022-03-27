package me.nemiron.khinkalyator.features.people.person.ui

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.compose.form.validation.control.isNotBlank
import me.aartikov.sesame.compose.form.validation.control.validation
import me.aartikov.sesame.compose.form.validation.form.HideErrorOnValueChanged
import me.aartikov.sesame.compose.form.validation.form.SetFocusOnFirstInvalidControlAfterValidation
import me.aartikov.sesame.compose.form.validation.form.formValidator
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.utils.TextTransformations.PersonName
import me.nemiron.khinkalyator.core.ui.utils.TextTransformations.PhoneNumber
import me.nemiron.khinkalyator.core.ui.utils.componentCoroutineScope
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.ObservePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.UpdatePersonUseCase

class RealPersonComponent(
    componentContext: ComponentContext,
    private val onOutput: (PersonComponent.Output) -> Unit,
    private val observePerson: ObservePersonUseCase,
    private val addPerson: AddPersonUseCase,
    private val updatePerson: UpdatePersonUseCase
) : PersonComponent, ComponentContext by componentContext {

    private var config: PersonComponent.Configuration by mutableStateOf(
        PersonComponent.Configuration.NewPerson()
    )

    private val coroutineScope = componentCoroutineScope()

    private var personSubscriptionJob: Job? = null

    override val nameInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        textTransformation = PersonName,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    // FIXME: correct textTransformation
    override val phoneInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Phone
        ),
        textTransformation = PhoneNumber
    )

    override val buttonText by derivedStateOf {
        val resourceId = when (config) {
            is PersonComponent.Configuration.EditPerson -> R.string.person_change_button
            is PersonComponent.Configuration.NewPerson -> R.string.person_add_button
        }
        LocalizedString.resource(resourceId)
    }

    override fun setConfiguration(config: PersonComponent.Configuration) {
        this.config = config
    }

    private val validator = coroutineScope.formValidator {
        features = listOf(HideErrorOnValueChanged, SetFocusOnFirstInvalidControlAfterValidation)

        input(nameInputControl) { isNotBlank(R.string.common_empty_error) }
        input(phoneInputControl) {
            val phoneRegex = Patterns.PHONE.toRegex()
            validation(
                isValid = {
                    it.isEmpty() || phoneRegex.matches(it)
                },
                R.string.common_value_is_not_valid_error
            )
        }
    }

    init {
        lifecycle.doOnCreate {
            snapshotFlow { config }
                .onEach(::onConfigChange)
                .launchIn(coroutineScope)
        }
    }

    override fun onSubmitClick() {
        if (validator.validate().isValid) {
            val name = nameInputControl.text
            val phone = phoneInputControl.text.takeIf { it.isNotBlank() }
            createOrUpdatePerson(config, name, phone)
            onOutput(PersonComponent.Output.PersonEdited)
        }
    }

    private fun onConfigChange(newConfig: PersonComponent.Configuration) {
        personSubscriptionJob?.cancel()

        if (newConfig is PersonComponent.Configuration.EditPerson) {
            personSubscriptionJob = coroutineScope.launch {
                observePerson(newConfig.personId)
                    .collect { person ->
                        person?.let {
                            initInputControlTexts(
                                name = person.name,
                                phone = person.phone?.value.orEmpty()
                            )
                        }
                    }
            }
        } else {
            initInputControlTexts(name = "", phone = "")
        }
    }

    private fun initInputControlTexts(name: String, phone: String) {
        nameInputControl.text = name
        nameInputControl.error = null
        nameInputControl.onFocusChanged(false)
        phoneInputControl.text = phone
        phoneInputControl.error = null
        phoneInputControl.onFocusChanged(false)
    }

    private fun createOrUpdatePerson(
        config: PersonComponent.Configuration,
        name: String,
        phone: String?
    ) {
        coroutineScope.launch {
            when (config) {
                is PersonComponent.Configuration.EditPerson -> {
                    updatePerson(personId = config.personId, name = name, phone = phone)
                }
                is PersonComponent.Configuration.NewPerson -> {
                    addPerson(name = name, phone = phone)
                }
            }
        }
    }
}