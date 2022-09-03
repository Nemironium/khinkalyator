package me.nemiron.khinkalyator.features.restaraunts.overview.ui

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.MenuDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantDetailsComponent

interface RestaurantOverviewComponent {

    val routerState: RouterState<*, Child>

    sealed interface Configuration : Parcelable {

        @Parcelize
        object NewRestaurant : Configuration

        @Parcelize
        class EditRestaurant(val restaurantId: RestaurantId) : Configuration
    }

    sealed interface Child {
        class RestaurantDetails(val component: RestaurantDetailsComponent) : Child
        class MenuDetails(val component: MenuDetailsComponent) : Child
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