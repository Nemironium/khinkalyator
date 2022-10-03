package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.RestaurantsStorage
import me.nemiron.khinkalyator.common_domain.exception.RestaurantInActiveMeetException
import me.nemiron.khinkalyator.common_domain.model.Address
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

class UpdateRestaurantUseCase(
    private val restaurantsStorage: RestaurantsStorage,
    private val meetsStorage: MeetsStorage
) {
    suspend operator fun invoke(
        restaurantId: RestaurantId,
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>
    ) {
        val isRestaurantAttemptsInAnyMeet = meetsStorage.isRestaurantAttemptsInAnyMeet(restaurantId)

        // TODO: allow to add new Dish without exception
        if (isRestaurantAttemptsInAnyMeet) {
            throw RestaurantInActiveMeetException
        } else {
            restaurantsStorage.updateRestaurant(
                id = restaurantId,
                newName = name,
                newAddress = address,
                newPhone = phone,
                newDishes = dishes
            )
        }
    }
}