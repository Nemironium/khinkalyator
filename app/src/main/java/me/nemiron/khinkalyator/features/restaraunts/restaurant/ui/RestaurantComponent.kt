package me.nemiron.khinkalyator.features.restaraunts.restaurant.ui


import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.dish.domain.DishId
import me.nemiron.khinkalyator.features.dish.ui.DishComponent
import me.nemiron.khinkalyator.features.dish.ui.DishViewData
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantId

interface RestaurantComponent {

    val dishComponent: DishComponent?

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
        class EditRestaurant(val restaurantId: RestaurantId) : Configuration
    }

    sealed interface Output {
        object RestaurantCloseRequested : Output
    }
}