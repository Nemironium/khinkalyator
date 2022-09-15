package me.nemiron.khinkalyator.features.restaraunts.home_page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.ObserveActiveRestaurantsUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId

class RealRestaurantsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (RestaurantsPageComponent.Output) -> Unit,
    observeActiveRestaurants: ObserveActiveRestaurantsUseCase
) : RestaurantsPageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val restaurantsState by observeActiveRestaurants().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    override val restaurantsViewData by derivedStateOf {
        restaurantsState.map(Restaurant::toRestaurantHomePageViewData)
    }

    override fun onRestaurantAddClick() {
        onOutput(RestaurantsPageComponent.Output.NewRestaurantRequested)
    }

    override fun onRestaurantClick(restaurantId: RestaurantId) {
        onOutput(RestaurantsPageComponent.Output.RestaurantRequested(restaurantId))
    }

    override fun onRestaurantCallClick(restaurantId: RestaurantId) {
        restaurantsState.find { it.id == restaurantId }?.phone?.let {
            // TODO: call to ExternalAppService
        }
    }

    override fun onRestaurantShareClick(restaurantId: RestaurantId) {
        restaurantsState.find { it.id == restaurantId }?.let {
            // TODO: call to ExternalAppService
        }
    }
}