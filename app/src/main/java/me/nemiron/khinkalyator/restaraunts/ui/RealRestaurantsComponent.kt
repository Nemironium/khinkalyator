package me.nemiron.khinkalyator.restaraunts.ui

import com.arkivanov.decompose.ComponentContext

class RealRestaurantsComponent(
    componentContext: ComponentContext,
    private val onOutput: (RestaurantsComponent.Output) -> Unit
) : RestaurantsComponent, ComponentContext by componentContext {

    override fun onAddRestaurantClick() {
        onOutput(RestaurantsComponent.Output.NewRestaurantRequested)
    }
}