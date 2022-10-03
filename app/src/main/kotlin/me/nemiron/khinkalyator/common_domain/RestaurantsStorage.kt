package me.nemiron.khinkalyator.common_domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.common_domain.model.Address
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

interface RestaurantsStorage {
    fun observeActiveRestaurants(): Flow<List<Restaurant>>

    suspend fun deleteRestaurant(id: RestaurantId)

    suspend fun addRestaurant(
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>,
        status: Restaurant.Status = Restaurant.Status.Active
    ): RestaurantId

    suspend fun updateRestaurant(
        id: RestaurantId,
        newName: String,
        newAddress: Address?,
        newPhone: Phone?,
        newDishes: List<Dish>
    )

    suspend fun getRestaurant(id: RestaurantId): Restaurant
}