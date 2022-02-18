package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.RouterState
import me.nemiron.khinkalyator.evening.ui.EveningComponent
import me.nemiron.khinkalyator.main.ui.MainComponent

interface RootComponent {
    val routerState: RouterState<*, Child>

    sealed interface Child {
        class Main(val component: MainComponent) : Child
        class Evening(val component: EveningComponent) : Child
    }
}