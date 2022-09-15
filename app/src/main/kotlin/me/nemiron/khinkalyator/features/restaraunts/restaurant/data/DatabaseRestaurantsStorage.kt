package me.nemiron.khinkalyator.features.restaraunts.restaurant.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.domain.Price
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantsStorage
import menemironkhinkalyator.data.db.SelectAllActiveWithDishes
import menemironkhinkalyator.data.db.SelectByIdWithDishes

class DatabaseRestaurantsStorage(
    db: KhinkalytorDatabase,
    private val coroutineDispatcher: CoroutineDispatcher
) : RestaurantsStorage {

    private val restaurantQueries = db.restaurantQueries

    private val dishQueries = db.dishQueries

    override fun observeActiveRestaurants(): Flow<List<Restaurant>> {
        return restaurantQueries
            .selectAllActiveWithDishes()
            .asFlow()
            .mapToList()
            .map { it.toRestaurants() }
    }

    override suspend fun deleteRestaurant(id: RestaurantId) {
        withContext(coroutineDispatcher) {
            restaurantQueries.delete(id)
        }
    }

    override suspend fun addRestaurant(
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>,
        status: Restaurant.Status
    ): RestaurantId {
        return withContext(coroutineDispatcher) {
            restaurantQueries.transactionWithResult {
                restaurantQueries.insertOrReplace(
                    id = null,
                    name = name,
                    address = address?.value,
                    phone = phone?.value,
                    type = status.code
                )
                val restaurantId = restaurantQueries.getLastInsertedId().executeAsOne()
                dishes.forEach { dish ->
                    dishQueries.insertOrReplace(
                        id = null,
                        restaurantId = restaurantId,
                        name = dish.name,
                        price = dish.price.value
                    )
                }
                restaurantId
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
        withContext(coroutineDispatcher) {
            restaurantQueries.transaction {
                restaurantQueries.updateName(newName, id)
                restaurantQueries.updateAddress(newAddress?.value, id)
                restaurantQueries.updatePhone(newPhone?.value, id)

                newDishes.forEach { dish ->
                    dishQueries.insertOrReplace(dish.id, id, dish.name, dish.price.value)
                }
            }
        }
    }

    override suspend fun getRestaurant(id: RestaurantId): Restaurant? {
        return withContext(coroutineDispatcher) {
            restaurantQueries
                .selectByIdWithDishes(id)
                .executeAsList()
                .toRestaurant()
        }
    }

    private fun List<SelectAllActiveWithDishes>.toRestaurants(): List<Restaurant> {
        return groupBy { it.id }
            .map { (restaurantId, selectRows) ->
                val rowData = selectRows.first()
                mapRestaurant(
                    id = rowData.id,
                    name = rowData.name,
                    addressValue = rowData.address,
                    phoneValue = rowData.phone,
                    type = rowData.type,
                    dishes = selectRows.map {
                        mapDish(it.id_, it.name_, it.price, it.type_)
                    }
                )
            }
    }

    private fun List<SelectByIdWithDishes>.toRestaurant(): Restaurant? {
        return groupBy { it.id }
            .map { (restaurantId, selectRows) ->
                val rowData = selectRows.first()
                mapRestaurant(
                    id = rowData.id,
                    name = rowData.name,
                    addressValue = rowData.address,
                    phoneValue = rowData.phone,
                    type = rowData.type,
                    dishes = selectRows.map {
                        mapDish(it.id_, it.name_, it.price, it.type_)
                    }
                )
            }
            .firstOrNull()
    }

    private fun mapRestaurant(
        id: RestaurantId,
        name: String,
        addressValue: String?,
        phoneValue: String?,
        type: Long,
        dishes: List<Dish>
    ): Restaurant {
        val status = when (type) {
            1L -> Restaurant.Status.Active
            else -> Restaurant.Status.Archived
        }
        return Restaurant(
            id,
            name,
            addressValue?.let(::Address),
            phoneValue?.let(::Phone),
            dishes,
            status
        )
    }

    private fun mapDish(
        id: DishId,
        name: String,
        priceValue: Double,
        type: Long
    ): Dish {
        val status = when (type) {
            1L -> Dish.Status.Active
            else -> Dish.Status.Archived
        }
        return Dish(id, name, Price(priceValue), null, status)
    }
}