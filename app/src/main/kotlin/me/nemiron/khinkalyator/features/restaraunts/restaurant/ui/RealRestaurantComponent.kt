package me.nemiron.khinkalyator.features.restaraunts.restaurant.ui

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import me.nemiron.khinkalyator.core.utils.TextTransformations
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.dish.domain.DishId
import me.nemiron.khinkalyator.features.dish.ui.DishComponent
import me.nemiron.khinkalyator.features.dish.ui.toDishViewData
import me.nemiron.khinkalyator.features.restaraunts.domain.AddRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.DeleteRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.GetRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.domain.UpdateRestaurantUseCase

class RealRestaurantComponent(
    componentContext: ComponentContext,
    private val configuration: RestaurantComponent.Configuration,
    private val onOutput: (RestaurantComponent.Output) -> Unit,
    private val getRestaurantById: GetRestaurantByIdUseCase,
    private val addRestaurant: AddRestaurantUseCase,
    private val updateRestaurant: UpdateRestaurantUseCase,
    private val deleteRestaurantById: DeleteRestaurantByIdUseCase
) : RestaurantComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    // TODO: add via componentHolder
    override val dishComponent: DishComponent? = null

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

    // FIXME: correct textTransformation
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

    private var menu: List<Dish> by mutableStateOf(emptyList())

    override val dishesViewData by derivedStateOf {
        menu.map(Dish::toDishViewData)
    }

    private var restaurantName: String? by mutableStateOf(null)

    override val title by derivedStateOf {
        restaurantName?.let {
            LocalizedString.raw(it)
        } ?: LocalizedString.resource(R.string.restaurant_title_new)
    }

    override val isButtonActive by derivedStateOf {
        when (configuration) {
            is RestaurantComponent.Configuration.EditRestaurant -> {
                // FIXME: compare loaded model and screen state
                true
            }
            is RestaurantComponent.Configuration.NewRestaurant -> {
                nameInputControl.text.isNotBlank()
            }
        }
    }

    override val isDeleteActionVisible by derivedStateOf {
        when (configuration) {
            is RestaurantComponent.Configuration.EditRestaurant -> true
            is RestaurantComponent.Configuration.NewRestaurant -> false
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
        lifecycle.doOnCreate(::loadRestaurant)
    }

    override fun onSubmitClick() {
        if (validator.validate().isValid) {
            coroutineScope.launch {
                val name = nameInputControl.text
                val address = addressInputControl.text.takeIf { it.isNotBlank() }
                val phone = phoneInputControl.text.takeIf { it.isNotBlank() }
                createOrUpdateRestaurant(
                    configuration = configuration,
                    name = name,
                    address = address,
                    phone = phone,
                    menu = menu
                )
            }
        }
    }

    override fun onRestaurantDeleteClick() {
        when (configuration) {
            is RestaurantComponent.Configuration.EditRestaurant -> {
                coroutineScope.launch {
                    deleteRestaurantById(configuration.restaurantId)
                    onOutput(RestaurantComponent.Output.RestaurantCloseRequested)
                }
            }
            is RestaurantComponent.Configuration.NewRestaurant -> {
                // nothing
            }
        }
    }

    override fun onDishAddClick() {
        // call dialogControl without data
    }

    override fun onDishClick(dishId: DishId) {
        // call dialogControl with data
    }

    private suspend fun createOrUpdateRestaurant(
        configuration: RestaurantComponent.Configuration,
        name: String,
        address: String?,
        phone: String?,
        menu: List<Dish>
    ) {
        when (configuration) {
            is RestaurantComponent.Configuration.EditRestaurant -> {
                updateRestaurant(
                    restaurantId = configuration.restaurantId,
                    name = name,
                    phone = phone,
                    address = address,
                    menu = menu
                )
            }
            is RestaurantComponent.Configuration.NewRestaurant -> {
                addRestaurant(
                    name = name,
                    address = address,
                    phone = phone,
                    menu = menu
                )
            }
        }
        onOutput(RestaurantComponent.Output.RestaurantCloseRequested)
    }

    private fun loadRestaurant() {
        when (configuration) {
            is RestaurantComponent.Configuration.EditRestaurant -> {
                coroutineScope.launch {
                    val restaurant = getRestaurantById(configuration.restaurantId)
                    restaurant?.let {
                        setTextFields(restaurant)
                        restaurantName = restaurant.name
                        menu = restaurant.menu
                    }
                }
            }
            is RestaurantComponent.Configuration.NewRestaurant -> {
                // nothing
            }
        }
    }

    private fun setTextFields(restaurant: Restaurant) {
        nameInputControl.text = restaurant.name
        phoneInputControl.text = restaurant.phone?.value.orEmpty()
        addressInputControl.text = restaurant.address?.value.orEmpty()
    }

    private fun odDishOutput(output: DishComponent.Output): Unit =
        when (output) {
            is DishComponent.Output.DishAdded -> {
                menu = menu.toMutableList().apply {
                    add(output.dish)
                }
            }
            is DishComponent.Output.DishCreatingCompleted -> {
                // TODO: close DishComponent BottomSheet
            }
            is DishComponent.Output.DishDeleted -> {
                menu = menu.toMutableList().apply {
                    remove(output.dish)
                }
            }
        }
}