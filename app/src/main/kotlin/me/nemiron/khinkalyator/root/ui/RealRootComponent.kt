package me.nemiron.khinkalyator.root.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.utils.log
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.home.createHomeComponent
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.createCreateMeetComponent
import me.nemiron.khinkalyator.features.meets.createMeetComponent
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetId
import me.nemiron.khinkalyator.features.restaraunts.createRestaurantOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.overview.ui.RestaurantOverviewComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = ChildConfiguration.Home,
        handleBackButton = true,
        childFactory = ::createChild
    ).log("Root")

    override val childStackState: ChildStack<*, RootComponent.Child> by stack.toComposeState(
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
            is ChildConfiguration.CreateMeet -> RootComponent.Child.CreateMeet(
                componentFactory.createCreateMeetComponent(
                    componentContext,
                    ::onCreateMeetOutput
                )
            )
            is ChildConfiguration.Meet -> RootComponent.Child.Meet(
                componentFactory.createMeetComponent(
                    componentContext, childConfig.meetId
                )
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
                navigation.push(ChildConfiguration.CreateMeet)
            }
            is HomeComponent.Output.MeetRequested -> {
                navigation.push(ChildConfiguration.Meet(output.meetId))
            }
            is HomeComponent.Output.NewRestaurantRequested -> {
                navigation.push(
                    ChildConfiguration.Restaurant(
                        RestaurantOverviewComponent.Configuration.NewRestaurant
                    )
                )
            }
            is HomeComponent.Output.RestaurantRequested -> {
                navigation.push(
                    ChildConfiguration.Restaurant(
                        RestaurantOverviewComponent.Configuration.EditRestaurant(output.restaurantId)
                    )
                )
            }
        }

    private fun onCreateMeetOutput(output: CreateMeetComponent.Output) =
        when (output) {
            is CreateMeetComponent.Output.MeetCreated -> {
                navigation.replaceCurrent(ChildConfiguration.Meet(output.meetId))
            }
            is CreateMeetComponent.Output.NewRestaurantRequested -> {
                navigation.push(
                    ChildConfiguration.Restaurant(
                        RestaurantOverviewComponent.Configuration.NewRestaurant
                    )
                )
            }
        }

    private fun onRestaurantOutput(output: RestaurantOverviewComponent.Output) =
        when (output) {
            is RestaurantOverviewComponent.Output.RestaurantCloseRequested -> navigation.pop()
        }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        object Home : ChildConfiguration

        @Parcelize
        object CreateMeet : ChildConfiguration

        @Parcelize
        class Meet(val meetId: MeetId) : ChildConfiguration

        @Parcelize
        class Restaurant(
            val restaurantConfiguration: RestaurantOverviewComponent.Configuration
        ) : ChildConfiguration
    }
}