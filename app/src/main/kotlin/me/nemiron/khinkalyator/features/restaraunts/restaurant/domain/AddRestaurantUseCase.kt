package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.common_domain.RestaurantsStorage
import me.nemiron.khinkalyator.common_domain.model.Address
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Phone

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