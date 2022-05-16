package me.nemiron.khinkalyator.features.home

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.home.ui.RealHomeComponent
import org.koin.core.component.get

fun ComponentFactory.createHomeComponent(
    componentContext: ComponentContext,
    onOutput: (HomeComponent.Output) -> Unit
): HomeComponent {
    return RealHomeComponent(componentContext, onOutput, get(), get())
}
