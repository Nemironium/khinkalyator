package me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui.RestaurantDishesComponent
import me.nemiron.khinkalyator.common_domain.model.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.details.ui.RestaurantDetailsComponent

interface RestaurantOverviewComponent {

    val childStack: ChildStack<*, Child>

    sealed interface Configuration : Parcelable {

        @Parcelize
        object NewRestaurant : Configuration

        @Parcelize
        class EditRestaurant(val restaurantId: RestaurantId) : Configuration
    }

    sealed interface Child {
        class RestaurantDetails(val component: RestaurantDetailsComponent) : Child
        class RestaurantDishes(val component: RestaurantDishesComponent) : Child
    }

    sealed interface Output {
        object RestaurantCloseRequested : Output
    }

    fun Configuration.toDetailsConfiguration(): RestaurantDetailsComponent.Configuration {
        return when (this) {
            is Configuration.EditRestaurant -> RestaurantDetailsComponent.Configuration.EditRestaurant
            is Configuration.NewRestaurant -> RestaurantDetailsComponent.Configuration.NewRestaurant
        }
    }
}