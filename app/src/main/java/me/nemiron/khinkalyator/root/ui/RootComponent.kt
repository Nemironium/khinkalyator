package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.RouterState
import me.nemiron.khinkalyator.home.ui.HomeComponent

interface RootComponent {
    val routerState: RouterState<*, Child>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
    }
}