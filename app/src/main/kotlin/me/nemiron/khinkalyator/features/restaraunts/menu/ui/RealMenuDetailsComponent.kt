package me.nemiron.khinkalyator.features.restaraunts.menu.ui

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
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.CreateDishUseCase
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId

class RealMenuDetailsComponent(
    componentContext: ComponentContext,
    configuration: MenuDetailsComponent.Configuration,
    private val state: MenuDetailsComponent.State,
    private val onOutput: (MenuDetailsComponent.Output) -> Unit,
    private val createDish: CreateDishUseCase
) : MenuDetailsComponent, ComponentContext by componentContext {

    private var selectedDish by mutableStateOf(
        getInitialDish(
            configuration,
            state.dishes
        )
    )

    override val dishesViewData by derivedStateOf {
        state.dishesViewData.map { dish ->
            val isShouldBeSelected = dish.id == selectedDish?.id
            dish.copy(isSelected = isShouldBeSelected)
        }
    }

    override val isDeleteActionVisible by derivedStateOf { selectedDish != null }

    override val topTitle = LocalizedString.resource(R.string.menu_details_title)

    override val bottomTitle by derivedStateOf {
        val resourceId = if (selectedDish != null) {
            R.string.menu_details_dish_title_edit
        } else {
            R.string.menu_details_dish_title_new
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

    private val coroutineScope = componentCoroutineScope()

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
            onOutput(MenuDetailsComponent.Output.MenuCloseRequested)
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
            setTextFields(null, true)
        }
    }

    private fun getInitialDish(
        configuration: MenuDetailsComponent.Configuration,
        allDishes: List<Dish>
    ): Dish? =
        when (configuration) {
            is MenuDetailsComponent.Configuration.AddDish -> {
                null
            }
            is MenuDetailsComponent.Configuration.EditDish -> {
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
        priceInputControl.text = dish?.price?.toString().orEmpty()
        if (focusToName) {
            nameInputControl.hasFocus = true
        }
    }
}