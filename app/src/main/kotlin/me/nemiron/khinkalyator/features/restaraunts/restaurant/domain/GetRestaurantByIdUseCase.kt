package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.common_domain.RestaurantsStorage
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

class GetRestaurantByIdUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(id: RestaurantId): Restaurant {
        return restaurantsStorage.getRestaurant(id)
    }
}