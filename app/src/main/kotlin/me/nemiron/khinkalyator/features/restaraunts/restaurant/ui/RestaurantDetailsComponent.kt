package me.nemiron.khinkalyator.features.restaraunts.restaurant.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.DishViewData
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address

interface RestaurantDetailsComponent {

    val nameInputControl: InputControl

    val addressInputControl: InputControl

    val phoneInputControl: InputControl

    val dishesViewData: List<DishViewData>

    val title: LocalizedString

    val isButtonActive: Boolean

    val isDeleteActionVisible: Boolean

    fun onSubmitClick()

    fun onRestaurantDeleteClick()

    fun onDishAddClick()

    fun onDishClick(dishId: DishId)

    sealed interface Configuration : Parcelable {

        @Parcelize
        object NewRestaurant : Configuration

        @Parcelize
        object EditRestaurant : Configuration
    }

    interface State {
        val name: String?
        val address: Address?
        val phone: Phone?
        val dishesViewData: List<DishViewData>

        fun setName(name: String)

        fun setAddress(address: Address)

        fun setPhone(phone: Phone)
    }

    sealed interface Output {
        object RestaurantDeleteRequested : Output
        object RestaurantSaveRequested : Output
        object NewDishRequested : Output
        class DishRequested(val dishId: DishId) : Output
    }
}