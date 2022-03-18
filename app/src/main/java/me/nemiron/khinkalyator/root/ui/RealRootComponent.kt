package me.nemiron.khinkalyator.root.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ui.utils.toComposeState
import me.nemiron.khinkalyator.home.ui.HomeComponent
import me.nemiron.khinkalyator.home.ui.RealHomeComponent

class RealRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val router = router<Configuration, RootComponent.Child>(
        initialConfiguration = Configuration.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: RouterState<*, RootComponent.Child> by router.state.toComposeState(
        lifecycle
    )

    private fun createChild(
        config: Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (config) {
            is Configuration.Home -> RootComponent.Child.Home(
                RealHomeComponent(
                    componentContext,
                    ::onHomeOutput
                )
            )
        }

    private fun onHomeOutput(output: HomeComponent.Output): Unit =
        when (output) {
            else -> { /* TODO */ }
        }


    private sealed class Configuration : Parcelable {
        @Parcelize
        object Home : Configuration()
    }
}