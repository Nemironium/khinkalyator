package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.RestaurantsStorage
import me.nemiron.khinkalyator.common_domain.exception.RestaurantInActiveMeetException
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

class DeleteRestaurantByIdUseCase(
    private val restaurantsStorage: RestaurantsStorage,
    private val meetsStorage: MeetsStorage
) {
    suspend operator fun invoke(restaurantId: RestaurantId) {
        val isRestaurantAttemptsInAnyMeet = meetsStorage.isRestaurantAttemptsInAnyMeet(restaurantId)

        if (isRestaurantAttemptsInAnyMeet) {
            throw RestaurantInActiveMeetException
        } else {
            restaurantsStorage.deleteRestaurant(restaurantId)
        }
    }
}