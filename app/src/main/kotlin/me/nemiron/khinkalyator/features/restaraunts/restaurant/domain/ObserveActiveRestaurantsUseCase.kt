package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.common_domain.RestaurantsStorage
import me.nemiron.khinkalyator.common_domain.model.Restaurant

class ObserveActiveRestaurantsUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    operator fun invoke(): Flow<List<Restaurant>> {
        return restaurantsStorage.observeActiveRestaurants()
    }
}