package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.RouterState
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.restaraunts.new.ui.NewRestaurantComponent

interface RootComponent {
    val routerState: RouterState<*, Child>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class NewRestaurant(val component: NewRestaurantComponent) : Child
    }
}