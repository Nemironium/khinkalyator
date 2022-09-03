package me.nemiron.khinkalyator.features.restaraunts.menu.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId

interface MenuDetailsComponent {
    val dishesViewData: List<DishViewData>

    val topTitle: LocalizedString

    val bottomTitle: LocalizedString

    val nameInputControl: InputControl

    val priceInputControl: InputControl

    val isDeleteActionVisible: Boolean

    fun onDishAddClick()

    fun onDishClick(dishId: DishId)

    fun onDishDeleteClick()

    fun onSubmitClick()

    fun onDishNextClick()

    sealed interface Configuration : Parcelable {

        @Parcelize
        object AddDish : Configuration

        @Parcelize
        class EditDish(val selectedDishId: DishId) : Configuration
    }

    interface State {
        val dishes: List<Dish>
        val dishesViewData: List<DishViewData>

        fun addDish(dish: Dish)

        fun editDish(dish: Dish)

        fun deleteDish(dish: Dish)
    }

    sealed interface Output {
        object MenuCloseRequested : Output
    }
}