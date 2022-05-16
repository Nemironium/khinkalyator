package me.nemiron.khinkalyator.features.restaraunts.domain

import kotlinx.coroutines.flow.Flow

class ObserveRestaurantsUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    operator fun invoke(): Flow<List<Restaurant>> {
        return restaurantsStorage.observeRestaurants()
    }
}