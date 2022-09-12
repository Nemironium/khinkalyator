package me.nemiron.khinkalyator.features.restaraunts

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.restaraunts.restaurant.data.InMemoryRestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui.RealRestaurantOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui.RestaurantOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.AddRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.DeleteRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.GetRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.ObserveRestaurantsUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.UpdateRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.home_page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.home_page.ui.RestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.details.ui.RealRestaurantDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.details.ui.RestaurantDetailsComponent
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

fun ComponentFactory.createRestaurantOverviewComponent(
    componentContext: ComponentContext,
    configuration: RestaurantOverviewComponent.Configuration,
    onOutput: (RestaurantOverviewComponent.Output) -> Unit
): RestaurantOverviewComponent {
    return RealRestaurantOverviewComponent(
        componentContext,
        configuration,
        onOutput,
        get(),
        get(),
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createRestaurantDetailsComponent(
    componentContext: ComponentContext,
    configuration: RestaurantDetailsComponent.Configuration,
    state: RestaurantDetailsComponent.State,
    onOutput: (RestaurantDetailsComponent.Output) -> Unit
): RestaurantDetailsComponent {
    return RealRestaurantDetailsComponent(
        componentContext,
        configuration,
        state,
        onOutput
    )
}