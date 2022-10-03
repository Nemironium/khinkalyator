package me.nemiron.khinkalyator.features.restaraunts.details.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.DishViewData
import me.nemiron.khinkalyator.common_domain.model.Address

interface RestaurantDetailsComponent {

    val nameInputControl: InputControl

    val addressInputControl: InputControl

    val phoneInputControl: InputControl

    val dishesViewData: List<DishViewData>

    val menuData: OverflowMenuData?

    val title: LocalizedString

    val isButtonActive: Boolean

    fun onSubmitClick()

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