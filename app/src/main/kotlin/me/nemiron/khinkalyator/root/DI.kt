package me.nemiron.khinkalyator.root

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.root.ui.RealRootComponent
import me.nemiron.khinkalyator.root.ui.RootComponent
import org.koin.core.component.get

fun ComponentFactory.createRootComponent(componentContext: ComponentContext): RootComponent {
    return RealRootComponent(componentContext, get())
}