package me.nemiron.khinkalyator.features.restaraunts.new.ui

import com.arkivanov.decompose.ComponentContext

class RealNewRestaurantComponent(
    componentContext: ComponentContext,
    private val onOutput: (NewRestaurantComponent.Output) -> Unit
) : NewRestaurantComponent, ComponentContext by componentContext {

    override fun onDeleteRestaurantClick() {
        onOutput(NewRestaurantComponent.Output.RestaurantDeleted)
    }
}