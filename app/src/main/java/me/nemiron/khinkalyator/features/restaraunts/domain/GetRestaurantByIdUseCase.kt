package me.nemiron.khinkalyator.features.restaraunts.domain

class GetRestaurantByIdUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(id: RestaurantId): Restaurant? {
        return restaurantsStorage.getRestaurant(id)
    }
}