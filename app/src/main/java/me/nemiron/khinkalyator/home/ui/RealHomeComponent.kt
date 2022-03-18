package me.nemiron.khinkalyator.home.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.core.ui.utils.log
import me.nemiron.khinkalyator.core.ui.utils.toComposeState
import me.nemiron.khinkalyator.meets.ui.RealMeetsComponent
import me.nemiron.khinkalyator.people.ui.RealPeopleComponent
import me.nemiron.khinkalyator.restaraunts.ui.RealRestaurantsComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val router = router<Configuration, HomeComponent.Child>(
        initialConfiguration = Configuration.Meets,
        handleBackButton = true,
        childFactory = ::createChild
    ).log("Home")

    override val routerState: RouterState<*, HomeComponent.Child> by router.state.toComposeState(
        lifecycle
    )

    // TODO: think about the need of field
    override val isTabsVisible = true

    override fun onPageSelected(page: HomeComponent.Page) {
        val configuration = when (page) {
            HomeComponent.Page.Meets -> Configuration.Meets
            HomeComponent.Page.People -> Configuration.People
            HomeComponent.Page.Restaurants -> Configuration.Restaurants
        }

        router.bringToFront(configuration)
    }

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): HomeComponent.Child {
        return when (configuration) {
            Configuration.Meets -> {
                HomeComponent.Child.Meets(RealMeetsComponent(componentContext))
            }
            Configuration.People -> {
                HomeComponent.Child.People(RealPeopleComponent(componentContext))
            }
            Configuration.Restaurants -> {
                HomeComponent.Child.Restaurants(RealRestaurantsComponent(componentContext))
            }
        }
    }

    private sealed interface Configuration : Parcelable {

        @Parcelize
        object Meets : Configuration

        @Parcelize
        object Restaurants : Configuration

        @Parcelize
        object People : Configuration
    }
}