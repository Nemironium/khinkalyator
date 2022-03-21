package me.nemiron.khinkalyator.restaraunts.page.ui

import com.arkivanov.decompose.ComponentContext

class RealRestaurantsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (RestaurantsPageComponent.Output) -> Unit
) : RestaurantsPageComponent, ComponentContext by componentContext {

    override fun onAddRestaurantClick() {
        onOutput(RestaurantsPageComponent.Output.NewRestaurantRequested)
    }
}