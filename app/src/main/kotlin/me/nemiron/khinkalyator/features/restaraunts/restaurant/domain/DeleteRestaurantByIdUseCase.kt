package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

class DeleteRestaurantByIdUseCase(
    private val restaurantsStorage: RestaurantsStorage,
) {
    suspend operator fun invoke(restaurantId: RestaurantId) {
        /*
        * TODO: check that restaurant doesn't attempt in any Meet
        *  if true – just delete it from storage
        *  if false – think about it :)
        * */
        restaurantsStorage.deleteRestaurant(restaurantId)
    }
}