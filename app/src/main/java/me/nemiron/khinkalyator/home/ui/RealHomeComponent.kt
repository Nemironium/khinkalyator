package me.nemiron.khinkalyator.home.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.nemiron.khinkalyator.meets.ui.RealMeetsComponent
import me.nemiron.khinkalyator.people.ui.RealPeopleComponent
import me.nemiron.khinkalyator.restaraunts.page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.restaraunts.page.ui.RestaurantsPageComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override val meetsComponent = RealMeetsComponent(
        childContext(key = "meetsPage")
    )

    override val restaurantsPageComponent = RealRestaurantsPageComponent(
        childContext(key = "restaurantsPage"),
        onOutput = ::onRestaurantsOutput
    )

    override val peopleComponent = RealPeopleComponent(
        childContext(key = "peoplePage")
    )

    private fun onRestaurantsOutput(output: RestaurantsPageComponent.Output) {
        when (output) {
            is RestaurantsPageComponent.Output.NewRestaurantRequested -> {
                onOutput(HomeComponent.Output.NewRestaurantRequested)
            }
        }
    }
}