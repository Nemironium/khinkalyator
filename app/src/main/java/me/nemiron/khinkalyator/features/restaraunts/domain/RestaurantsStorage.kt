package me.nemiron.khinkalyator.features.restaraunts.domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

interface RestaurantsStorage {
    fun observeRestaurants(): Flow<List<Restaurant>>

    suspend fun deleteRestaurant(id: RestaurantId)

    suspend fun addRestaurant(
        name: String,
        address: Address?,
        phone: Phone?,
        menu: List<Dish>
    )

    suspend fun updateRestaurant(
        id: RestaurantId,
        newName: String,
        newAddress: Address?,
        newPhone: Phone?,
        newMenu: List<Dish>
    )

    suspend fun getRestaurant(id: RestaurantId): Restaurant?
}