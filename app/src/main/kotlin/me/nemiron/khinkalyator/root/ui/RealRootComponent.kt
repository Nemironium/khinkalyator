package me.nemiron.khinkalyator.root.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ui.keyboard.CloseKeyboardServiceImpl
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.home.ui.RealHomeComponent
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.restaraunts.domain.AddRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.DeleteRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.GetRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.domain.UpdateRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RealRestaurantComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val peopleStorage: PeopleStorage,
    private val restaurantsStorage: RestaurantsStorage
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
                RealHomeComponent(
                    componentContext,
                    peopleStorage = peopleStorage,
                    restaurantsStorage = restaurantsStorage,
                    closeKeyboardService = CloseKeyboardServiceImpl(),
                    onOutput = ::onHomeOutput
                )
            )
            is ChildConfiguration.Restaurant -> RootComponent.Child.Restaurant(
                RealRestaurantComponent(
                    componentContext,
                    configuration = childConfig.restaurantConfiguration,
                    getRestaurantById = GetRestaurantByIdUseCase(restaurantsStorage),
                    addRestaurant = AddRestaurantUseCase(restaurantsStorage),
                    updateRestaurant = UpdateRestaurantUseCase(restaurantsStorage),
                    deleteRestaurantById = DeleteRestaurantByIdUseCase(restaurantsStorage),
                    onOutput = ::onNewRestaurantOutput
                )
            )
        }

    private fun onHomeOutput(output: HomeComponent.Output): Unit =
        when (output) {
            is HomeComponent.Output.NewRestaurantRequested -> {
                router.push(
                    ChildConfiguration.Restaurant(
                        RestaurantComponent.Configuration.NewRestaurant
                    )
                )
            }
            is HomeComponent.Output.NewMeetRequested -> {
                // TODO
            }
            is HomeComponent.Output.RestaurantRequested -> {
                router.push(
                    ChildConfiguration.Restaurant(
                        RestaurantComponent.Configuration.EditRestaurant(output.restaurantId)
                    )
                )
            }
        }

    private fun onNewRestaurantOutput(output: RestaurantComponent.Output): Unit =
        when (output) {
            is RestaurantComponent.Output.RestaurantCloseRequested -> {
                router.pop()
            }
        }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        object Home : ChildConfiguration

        @Parcelize
        class Restaurant(
            val restaurantConfiguration: RestaurantComponent.Configuration
        ) : ChildConfiguration
    }
}