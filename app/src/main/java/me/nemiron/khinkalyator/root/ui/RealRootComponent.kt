package me.nemiron.khinkalyator.root.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ui.utils.toComposeState
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.home.ui.RealHomeComponent
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.restaraunts.new.ui.NewRestaurantComponent
import me.nemiron.khinkalyator.features.restaraunts.new.ui.RealNewRestaurantComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val peopleStorage: PeopleStorage
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
                    peopleStorage = peopleStorage,
                    onOutput = ::onHomeOutput
                )
            )
            is Configuration.NewRestaurant -> RootComponent.Child.NewRestaurant(
                RealNewRestaurantComponent(
                    componentContext,
                    ::onNewRestaurantOutput
                )
            )
        }

    private fun onHomeOutput(output: HomeComponent.Output): Unit =
        when (output) {
            HomeComponent.Output.NewRestaurantRequested -> {
                router.push(
                    Configuration.NewRestaurant
                )
            }
            HomeComponent.Output.NewMeetRequested -> {
                // TODO
            }
        }

    private fun onNewRestaurantOutput(output: NewRestaurantComponent.Output): Unit =
        when (output) {
            NewRestaurantComponent.Output.RestaurantDeleted -> {
                // TODO: add DB entity deleting
                router.pop()
            }
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Home : Configuration()

        @Parcelize
        object NewRestaurant : Configuration()
    }
}