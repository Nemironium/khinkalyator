package me.nemiron.khinkalyator.features.restaraunts.restaurant.ui

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.compose.form.validation.control.isNotBlank
import me.aartikov.sesame.compose.form.validation.control.validation
import me.aartikov.sesame.compose.form.validation.form.HideErrorOnValueChanged
import me.aartikov.sesame.compose.form.validation.form.SetFocusOnFirstInvalidControlAfterValidation
import me.aartikov.sesame.compose.form.validation.form.formValidator
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.TextTransformations
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.DishViewData
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address

class RealRestaurantDetailsComponent(
    componentContext: ComponentContext,
    private val configuration: RestaurantDetailsComponent.Configuration,
    private val state: RestaurantDetailsComponent.State,
    private val onOutput: (RestaurantDetailsComponent.Output) -> Unit
) : RestaurantDetailsComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val stateData by derivedStateOf {
        RestaurantStateData(state.name, state.address, state.phone)
    }

    override val nameInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    override val addressInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
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
        textTransformation = TextTransformations.PhoneNumber
    )

    override val dishesViewData: List<DishViewData>
        get() = state.dishesViewData

    override val title by derivedStateOf {
        state.name?.ifBlank { null }?.let {
            LocalizedString.raw(it)
        } ?: LocalizedString.resource(R.string.restaurant_details_title_new)
    }

    override val isButtonActive by derivedStateOf {
        when (configuration) {
            is RestaurantDetailsComponent.Configuration.EditRestaurant -> {
                true
            }
            is RestaurantDetailsComponent.Configuration.NewRestaurant -> {
                !state.name.isNullOrBlank()
            }
        }
    }

    override val menuData by derivedStateOf {
        when (configuration) {
            is RestaurantDetailsComponent.Configuration.EditRestaurant -> OverflowMenuData(
                title = LocalizedString.resource(R.string.restaurant_details_delete_menu_action),
                onMenuItemClick = ::onRestaurantDeleteClick
            )
            is RestaurantDetailsComponent.Configuration.NewRestaurant -> null
        }
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
        // TODO: maybe it can be simpler
        lifecycle.doOnCreate {
            snapshotFlow { stateData }
                .onEach(::setTextFields)
                .launchIn(coroutineScope)

            snapshotFlow { nameInputControl.text }
                .onEach(state::setName)
                .launchIn(coroutineScope)

            snapshotFlow { addressInputControl.text }
                .onEach { state.setAddress(Address(it)) }
                .launchIn(coroutineScope)

            snapshotFlow { phoneInputControl.text }
                .onEach { state.setPhone(Phone(it)) }
                .launchIn(coroutineScope)
        }
    }

    override fun onSubmitClick() {
        if (validator.validate().isValid) {
            onOutput(RestaurantDetailsComponent.Output.RestaurantSaveRequested)
        }
    }

    override fun onDishAddClick() {
        onOutput(RestaurantDetailsComponent.Output.NewDishRequested)
    }

    override fun onDishClick(dishId: DishId) {
        onOutput(RestaurantDetailsComponent.Output.DishRequested(dishId))
    }

    private fun onRestaurantDeleteClick() {
        when (configuration) {
            is RestaurantDetailsComponent.Configuration.EditRestaurant -> {
                onOutput(RestaurantDetailsComponent.Output.RestaurantDeleteRequested)
            }
            is RestaurantDetailsComponent.Configuration.NewRestaurant -> {
                // nothing
            }
        }
    }

    private fun setTextFields(stateData: RestaurantStateData) {
        nameInputControl.text = stateData.name.orEmpty()
        phoneInputControl.text = stateData.phone?.value.orEmpty()
        addressInputControl.text = stateData.address?.value.orEmpty()
    }

    private data class RestaurantStateData(
        val name: String?,
        val address: Address?,
        val phone: Phone?,
    )
}