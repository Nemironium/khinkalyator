package me.nemiron.khinkalyator.main.ui

import com.arkivanov.decompose.ComponentContext

class RealMainComponent(
    componentContext: ComponentContext,
    private val onOutput: (MainComponent.Output) -> Unit
) : MainComponent, ComponentContext by componentContext {

}