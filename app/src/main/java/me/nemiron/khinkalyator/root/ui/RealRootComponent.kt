package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.evening.ui.RealEveningComponent
import me.nemiron.khinkalyator.main.ui.MainComponent
import me.nemiron.khinkalyator.main.ui.RealMainComponent

class RealRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val router = router<Configuration, RootComponent.Child>(
        initialConfiguration = Configuration.Main,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: Value<RouterState<*, RootComponent.Child>> = router.state

    private fun createChild(config: Configuration, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Configuration.Evening -> RootComponent.Child.Evening(
                RealEveningComponent(
                    componentContext,
                    config.eveningId
                )
            )
            is Configuration.Main -> RootComponent.Child.Main(
                RealMainComponent(
                    componentContext,
                    ::onMainOutput
                )
            )
        }

    private fun onMainOutput(output: MainComponent.Output): Unit =
        when (output) {
            is MainComponent.Output.EveningSelected -> router.push(Configuration.Evening(eveningId = output.eveningId))
        }


    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Evening(val eveningId: Long) : Configuration()
    }
}