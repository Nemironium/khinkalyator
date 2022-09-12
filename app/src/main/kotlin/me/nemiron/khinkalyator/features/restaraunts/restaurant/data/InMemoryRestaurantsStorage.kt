package me.nemiron.khinkalyator.features.restaraunts.restaurant.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantsStorage
import kotlin.random.Random

class InMemoryRestaurantsStorage : RestaurantsStorage {

    private val mockedRestaurants = Restaurant.MOCKS.associateBy(Restaurant::id)

    private val stateFlow = MutableStateFlow(mockedRestaurants)

    private val restaurantsMutex = Mutex()

    override fun observeRestaurants(): Flow<List<Restaurant>> {
        return stateFlow
            .asStateFlow()
            .map { it.values.toList() }
    }

    override suspend fun deleteRestaurant(id: RestaurantId) {
        restaurantsMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply { remove(id) }
        }
    }

    override suspend fun addRestaurant(
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>
    ) {
        restaurantsMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val id = Random.nextLong()
                    val restaurant = Restaurant(id, name, address, phone, dishes)
                    put(id, restaurant)
                }
        }
    }

    override suspend fun updateRestaurant(
        id: RestaurantId,
        newName: String,
        newAddress: Address?,
        newPhone: Phone?,
        newDishes: List<Dish>
    ) {
        restaurantsMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val previousRestaurant = get(id)
                    previousRestaurant?.let {
                        val updatedPerson = previousRestaurant.copy(
                            name = newName,
                            address = newAddress,
                            phone = newPhone,
                            dishes = newDishes
                        )
                        put(id, updatedPerson)
                    }
                }
        }
    }

    override suspend fun getRestaurant(id: RestaurantId): Restaurant? {
        return stateFlow
            .asStateFlow()
            .map { restaurantsMap ->
                restaurantsMap[id]
            }
            .first()
    }
}