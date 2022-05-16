package me.nemiron.khinkalyator.features.meets

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.page.ui.RealMeetsPageComponent

fun ComponentFactory.createMeetsPageComponent(
    componentContext: ComponentContext,
    onOutput: (MeetsPageComponent.Output) -> Unit
): MeetsPageComponent {
    return RealMeetsPageComponent(componentContext, onOutput)
}