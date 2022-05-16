package me.nemiron.khinkalyator.features.restaraunts

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.restaraunts.data.InMemoryRestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.domain.AddRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.DeleteRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.GetRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.ObserveRestaurantsUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.UpdateRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RealRestaurantComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val restaurantsModule = module {
    singleOf(::InMemoryRestaurantsStorage) { bind<RestaurantsStorage>() }
    factoryOf(::AddRestaurantUseCase)
    factoryOf(::DeleteRestaurantByIdUseCase)
    factoryOf(::GetRestaurantByIdUseCase)
    factoryOf(::ObserveRestaurantsUseCase)
    factoryOf(::UpdateRestaurantUseCase)
}

fun ComponentFactory.createRestaurantsPageComponent(
    componentContext: ComponentContext,
    onOutput: (RestaurantsPageComponent.Output) -> Unit
): RestaurantsPageComponent {
    return RealRestaurantsPageComponent(componentContext, onOutput, get())
}

fun ComponentFactory.createRestaurantComponent(
    componentContext: ComponentContext,
    configuration: RestaurantComponent.Configuration,
    onOutput: (RestaurantComponent.Output) -> Unit
): RestaurantComponent {
    return RealRestaurantComponent(
        componentContext,
        configuration,
        onOutput,
        get(),
        get(),
        get(),
        get()
    )
}