package me.nemiron.khinkalyator.features.dishes

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.dishes.domain.CreateDishUseCase
import me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui.RealRestaurantDishesComponent
import me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui.RestaurantDishesComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dishesModule = module {
    factoryOf(::CreateDishUseCase)
}

fun ComponentFactory.createRestaurantDishesComponent(
    componentContext: ComponentContext,
    configuration: RestaurantDishesComponent.Configuration,
    state: RestaurantDishesComponent.State,
    onOutput: (RestaurantDishesComponent.Output) -> Unit
): RestaurantDishesComponent {
    return RealRestaurantDishesComponent(
        componentContext,
        configuration,
        state,
        onOutput,
        get()
    )
}