package me.nemiron.khinkalyator.features.home.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.page.ui.RealMeetsPageComponent
import me.nemiron.khinkalyator.features.people.page.ui.RealPeoplePageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    /*
    * TODO: think about separate lifecycle for page components
    * */

    override val meetsPageComponent = RealMeetsPageComponent(
        childContext(key = "meetsPage"),
        onOutput = ::onMeetsOutput
    )

    override val restaurantsPageComponent = RealRestaurantsPageComponent(
        childContext(key = "restaurantsPage"),
        onOutput = ::onRestaurantsOutput
    )

    override val peoplePageComponent = RealPeoplePageComponent(
        childContext(key = "peoplePage")
    )

    private fun onMeetsOutput(output: MeetsPageComponent.Output) {
        when (output) {
            is MeetsPageComponent.Output.NewMeetRequested -> {
                onOutput(HomeComponent.Output.NewMeetRequested)
            }
            is MeetsPageComponent.Output.MeetRequested -> {
                // TODO
            }
        }
    }

    private fun onRestaurantsOutput(output: RestaurantsPageComponent.Output) {
        when (output) {
            is RestaurantsPageComponent.Output.NewRestaurantRequested -> {
                onOutput(HomeComponent.Output.NewRestaurantRequested)
            }
            is RestaurantsPageComponent.Output.RestaurantRequested -> {
                // TODO
            }
        }
    }
}