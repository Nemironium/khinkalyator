package me.nemiron.khinkalyator.features.restaraunts.restaurant.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Price
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantsStorage
import kotlin.random.Random

class InMemoryRestaurantsStorage : RestaurantsStorage {

    private val mockedRestaurants = listOf(
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("ул. Рубинштейна, 24"),
            phone = Phone("89219650524"),
            menu = listOf(
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с грибами и сыром",
                    price = Price(95.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с бараниной",
                    price = Price(90.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-аджарски S",
                    price = Price(450.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Чай в чайнике",
                    price = Price(250.0)
                )
            )
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("Большой проспект П.С., 82"),
            phone = null,
            menu = listOf(
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали со шпинатом и сыром",
                    price = Price(125.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с говядиной и свининой",
                    price = Price(85.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-аджарски M",
                    price = Price(550.0)
                )
            )
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Пхали-хинкали",
            address = Address("Большая Морская ул., 27"),
            phone = Phone("89219650524"),
            menu = listOf(
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с бараниной",
                    price = Price(50.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с сыром",
                    price = Price(55.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с говядиной и свининой",
                    price = Price(45.0)
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-мегрельски",
                    price = Price(450.0)
                )
            )
        )
    ).associateBy { it.id }

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
                            menu = newDishes
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