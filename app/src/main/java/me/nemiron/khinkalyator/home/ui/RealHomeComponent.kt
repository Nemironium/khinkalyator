package me.nemiron.khinkalyator.home.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.nemiron.khinkalyator.meets.ui.RealMeetsComponent
import me.nemiron.khinkalyator.people.ui.RealPeopleComponent
import me.nemiron.khinkalyator.restaraunts.ui.RealRestaurantsComponent
import me.nemiron.khinkalyator.restaraunts.ui.RestaurantsComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override val meetsComponent = RealMeetsComponent(
        childContext(key = "meetsPage")
    )

    override val restaurantsComponent = RealRestaurantsComponent(
        childContext(key = "restaurantsPage"),
        onOutput = ::onRestaurantsOutput
    )

    override val peopleComponent = RealPeopleComponent(
        childContext(key = "peoplePage")
    )

    private fun onRestaurantsOutput(output: RestaurantsComponent.Output) {
        when (output) {
            is RestaurantsComponent.Output.NewRestaurantRequested -> {
                onOutput(HomeComponent.Output.NewRestaurantRequested)
            }
        }
    }
}