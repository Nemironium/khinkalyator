package me.nemiron.khinkalyator.root.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.home.createHomeComponent
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.restaraunts.createRestaurantOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.overview.ui.RestaurantOverviewComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : RootComponent, ComponentContext by componentContext {

    private val router = router<ChildConfiguration, RootComponent.Child>(
        initialConfiguration = ChildConfiguration.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: RouterState<*, RootComponent.Child> by router.state.toComposeState(
        lifecycle
    )

    private fun createChild(
        childConfig: ChildConfiguration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (childConfig) {
            is ChildConfiguration.Home -> RootComponent.Child.Home(
                componentFactory.createHomeComponent(componentContext, ::onHomeOutput)
            )
            is ChildConfiguration.Restaurant -> RootComponent.Child.Restaurant(
                componentFactory.createRestaurantOverviewComponent(
                    componentContext,
                    childConfig.restaurantConfiguration,
                    ::onRestaurantOutput
                )
            )
        }

    private fun onHomeOutput(output: HomeComponent.Output) =
        when (output) {
            is HomeComponent.Output.NewMeetRequested -> {
                // TODO
            }
            is HomeComponent.Output.NewRestaurantRequested -> {
                router.push(
                    ChildConfiguration.Restaurant(
                        RestaurantOverviewComponent.Configuration.NewRestaurant
                    )
                )
            }
            is HomeComponent.Output.RestaurantRequested -> {
                router.push(
                    ChildConfiguration.Restaurant(
                        RestaurantOverviewComponent.Configuration.EditRestaurant(output.restaurantId)
                    )
                )
            }
        }

    private fun onRestaurantOutput(output: RestaurantOverviewComponent.Output) =
        when (output) {
            is RestaurantOverviewComponent.Output.RestaurantCloseRequested -> router.pop()
        }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        object Home : ChildConfiguration

        @Parcelize
        class Restaurant(
            val restaurantConfiguration: RestaurantOverviewComponent.Configuration
        ) : ChildConfiguration
    }
}