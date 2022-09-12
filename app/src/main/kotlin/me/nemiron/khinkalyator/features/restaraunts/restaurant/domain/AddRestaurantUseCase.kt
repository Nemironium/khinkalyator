package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

class AddRestaurantUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>
    ) {
        restaurantsStorage.addRestaurant(
            name = name,
            address = address,
            phone = phone,
            dishes = dishes
        )
    }
}