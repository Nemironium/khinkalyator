package me.nemiron.khinkalyator.features.restaraunts.domain

import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

class AddRestaurantUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(
        name: String,
        address: String?,
        phone: String?,
        menu: List<Dish>
    ) {
        restaurantsStorage.addRestaurant(
            name = name,
            address = address?.let { Address(it) },
            phone = phone?.let { Phone(it) },
            menu = menu
        )
    }
}