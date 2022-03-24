package me.nemiron.khinkalyator.features.home.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.nemiron.khinkalyator.features.meets.ui.RealMeetsComponent
import me.nemiron.khinkalyator.features.people.page.ui.RealPeoplePageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent

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

    override val peoplePageComponent = RealPeoplePageComponent(
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