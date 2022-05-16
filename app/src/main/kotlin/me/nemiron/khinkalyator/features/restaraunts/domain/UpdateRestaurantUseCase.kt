package me.nemiron.khinkalyator.features.restaraunts.domain

import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

class UpdateRestaurantUseCase(
    private val restaurantsStorage: RestaurantsStorage
) {
    suspend operator fun invoke(
        restaurantId: RestaurantId,
        name: String,
        address: String?,
        phone: String?,
        menu: List<Dish>
    ) {
        restaurantsStorage.updateRestaurant(
            id = restaurantId,
            newName = name,
            newAddress = address?.let { Address(it) },
            newPhone = phone?.let { Phone(it) },
            newMenu = menu
        )
    }
}