package me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui

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
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.features.dishes.domain.CreateDishUseCase
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.DishesComponent

// FIXME: насколько такая "композиция" из DishesComponent норм?
class RealRestaurantDishesComponent(
    componentContext: ComponentContext,
    configuration: RestaurantDishesComponent.Configuration,
    private val state: RestaurantDishesComponent.State,
    private val onOutput: (RestaurantDishesComponent.Output) -> Unit,
    private val createDish: CreateDishUseCase
) : RestaurantDishesComponent, DishesComponent, ComponentContext by componentContext {

    private var selectedDish by mutableStateOf(
        getInitialDish(
            configuration,
            state.dishes
        )
    )

    private val coroutineScope = componentCoroutineScope()

    override val dishesViewData by derivedStateOf {
        state.dishesViewData.map { dish ->
            val isShouldBeSelected = dish.id == selectedDish?.id
            dish.copy(isSelected = isShouldBeSelected)
        }
    }

    override val isDeleteActionVisible by derivedStateOf { selectedDish != null }

    override val dishesComponent = this

    override val topTitle = LocalizedString.resource(R.string.restaurant_dishes_title)

    override val bottomTitle by derivedStateOf {
        val resourceId = if (selectedDish != null) {
            R.string.restaurant_dishes_dish_title_edit
        } else {
            R.string.restaurant_dishes_dish_title_new
        }
        LocalizedString.resource(resourceId)
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
    override val priceInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        )
    )

    private val validator = coroutineScope.formValidator {
        features = listOf(HideErrorOnValueChanged, SetFocusOnFirstInvalidControlAfterValidation)

        input(nameInputControl) {
            isNotBlank(LocalizedString.empty())
        }
        input(priceInputControl) {
            isNotBlank(LocalizedString.empty())

            validation(
                isValid = {
                    val price = it.toDoubleOrNull()
                    price != null && price >= 0f
                },
                LocalizedString.empty()
            )
        }
    }

    init {
        lifecycle.doOnCreate {
            snapshotFlow { selectedDish }
                .onEach { setTextFields(it) }
                .launchIn(coroutineScope)
        }
    }

    override fun onDishAddClick() {
        selectedDish = null
    }

    override fun onDishClick(dishId: DishId) {
        state.dishes
            .firstOrNull { it.id == dishId }
            ?.let { selectedDish = it }
    }

    override fun onDishDeleteClick() {
        selectedDish?.let { dish ->
            state.dishes
                .firstOrNull { it.id == dish.id }
                ?.let {
                    selectedDish = null
                    state.deleteDish(it)
                }
        }
    }

    override fun onSubmitClick() {
        if (validator.validate().isValid) {
            mutateState()
            onOutput(RestaurantDishesComponent.Output.DishesCloseRequested)
        }
    }

    override fun onDishNextClick() {
        if (validator.validate().isValid) {
            mutateState()
            /*
            * Сейчас проблема в том, что snapshotFlow работает как distinctUntilChanged.
            *  Следовательно при вызове onDishNextClick при изначальном selectedDish == null
            *  snapshotFlow не отработает
            *  TODO: подумай, как можно этого избежать
            * */
            selectedDish = null
            setTextFields(null, true)
        }
    }

    private fun getInitialDish(
        configuration: RestaurantDishesComponent.Configuration,
        allDishes: List<Dish>
    ): Dish? =
        when (configuration) {
            is RestaurantDishesComponent.Configuration.AddDish -> {
                null
            }
            is RestaurantDishesComponent.Configuration.EditDish -> {
                allDishes.firstOrNull { it.id == configuration.selectedDishId }
            }
        }

    private fun mutateState() {
        val oldDish = selectedDish
        val newDish = getDishFromTextField(oldDish?.id)

        if (oldDish == null) {
            state.addDish(newDish)
        } else {
            state.editDish(newDish)
        }
    }

    private fun getDishFromTextField(previousDishId: DishId?): Dish {
        val name = nameInputControl.text
        val price = priceInputControl.text.toDouble()

        return createDish(name, price, previousDishId)
    }

    private fun setTextFields(dish: Dish?, focusToName: Boolean = false) {
        nameInputControl.text = dish?.name.orEmpty()
        priceInputControl.text = dish?.price?.value?.toString().orEmpty()
        if (focusToName) {
            nameInputControl.hasFocus = true
        }
    }
}