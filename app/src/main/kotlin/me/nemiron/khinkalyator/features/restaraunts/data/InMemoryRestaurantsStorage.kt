package me.nemiron.khinkalyator.features.restaraunts.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantsStorage
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
                    price = 95.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с бараниной",
                    price = 90.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-аджарски S",
                    price = 450.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Чай в чайнике",
                    price = 250.0
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
                    price = 125.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с говядиной и свининой",
                    price = 85.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-аджарски M",
                    price = 550.0
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
                    price = 50.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с сыром",
                    price = 55.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хинкали с говядиной и свининой",
                    price = 45.0
                ),
                Dish(
                    id = Random.nextLong(),
                    name = "Хачапури по-мегрельски",
                    price = 450.0
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
        menu: List<Dish>
    ) {
        restaurantsMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val id = Random.nextLong()
                    val restaurant = Restaurant(id, name, address, phone, menu)
                    put(id, restaurant)
                }
        }
    }

    override suspend fun updateRestaurant(
        id: RestaurantId,
        newName: String,
        newAddress: Address?,
        newPhone: Phone?,
        newMenu: List<Dish>
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
                            menu = newMenu
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