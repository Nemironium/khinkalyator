package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

class UpdateRestaurantUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(
        restaurantId: RestaurantId,
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>
    ) {
        restaurantsStorage.updateRestaurant(
            id = restaurantId,
            newName = name,
            newAddress = address,
            newPhone = phone,
            newDishes = dishes
        )
    }
}