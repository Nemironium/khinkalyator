package me.nemiron.khinkalyator.features.people.person.ui

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.launch
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.compose.form.validation.control.isNotBlank
import me.aartikov.sesame.compose.form.validation.control.validation
import me.aartikov.sesame.compose.form.validation.form.HideErrorOnValueChanged
import me.aartikov.sesame.compose.form.validation.form.SetFocusOnFirstInvalidControlAfterValidation
import me.aartikov.sesame.compose.form.validation.form.formValidator
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.TextTransformations.PersonName
import me.nemiron.khinkalyator.core.utils.TextTransformations.PhoneNumber
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.GetPersonByIdUseCase
import me.nemiron.khinkalyator.features.people.domain.UpdatePersonUseCase

class RealPersonComponent(
    componentContext: ComponentContext,
    private val configuration: PersonComponent.Configuration,
    private val onOutput: (PersonComponent.Output) -> Unit,
    private val getPersonById: GetPersonByIdUseCase,
    private val addPerson: AddPersonUseCase,
    private val updatePerson: UpdatePersonUseCase
) : PersonComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

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
        val resourceId = when (configuration) {
            is PersonComponent.Configuration.EditPerson -> R.string.person_change_button
            is PersonComponent.Configuration.NewPerson -> R.string.person_add_button
        }
        LocalizedString.resource(resourceId)
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
        lifecycle.doOnCreate(::setTextFields)
    }

    override fun onSubmitClick() {
        if (validator.validate().isValid) {
            coroutineScope.launch {
                val name = nameInputControl.text
                val phone = phoneInputControl.text.takeIf { it.isNotBlank() }
                createOrUpdatePerson(configuration, name, phone)
                onOutput(PersonComponent.Output.PersonEdited)
            }
        }
    }

    private fun setTextFields() {
        coroutineScope.launch {
            when (configuration) {
                is PersonComponent.Configuration.EditPerson -> {
                    val person = getPersonById(configuration.personId)
                    nameInputControl.text = person.name
                    phoneInputControl.text = person.phone?.value.orEmpty()
                }
                is PersonComponent.Configuration.NewPerson -> {
                    // nothing
                }
            }
        }
    }

    private suspend fun createOrUpdatePerson(
        config: PersonComponent.Configuration,
        name: String,
        phone: String?
    ) {
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