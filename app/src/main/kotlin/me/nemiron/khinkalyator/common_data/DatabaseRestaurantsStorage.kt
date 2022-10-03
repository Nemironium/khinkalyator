package me.nemiron.khinkalyator.common_data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.common_data.mapper.getTypeAndValue
import me.nemiron.khinkalyator.common_data.mapper.toRestaurants
import me.nemiron.khinkalyator.common_data.utils.suspendDbCall
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.Address
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId
import me.nemiron.khinkalyator.common_domain.RestaurantsStorage

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
        suspendDbCall {
            withContext(coroutineDispatcher) {
                restaurantQueries.delete(id)
            }
        }
    }

    override suspend fun addRestaurant(
        name: String,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>,
        status: Restaurant.Status
    ): RestaurantId {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
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
                        val (discountType, discountValue) = dish.discount.getTypeAndValue()
                        dishQueries.insertOrReplace(
                            id = null,
                            restaurantId = restaurantId,
                            name = dish.name,
                            price = dish.price.value,
                            discountType = discountType,
                            discountValue = discountValue,
                            type = Dish.Status.Active.code
                        )
                    }
                    restaurantId
                }
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
        suspendDbCall {
            restaurantQueries.transaction {
                restaurantQueries.updateName(newName, id)
                restaurantQueries.updateAddress(newAddress?.value, id)
                restaurantQueries.updatePhone(newPhone?.value, id)

                dishQueries.deleteDishesFromRestaurant(id)
                newDishes.forEach { dish ->
                    val (discountType, discountValue) = dish.discount.getTypeAndValue()
                    dishQueries.insertOrReplace(
                        id = dish.id,
                        restaurantId = id,
                        name = dish.name,
                        price = dish.price.value,
                        discountType = discountType,
                        discountValue = discountValue,
                        type = dish.status.code
                    )
                }
            }
        }
    }

    override suspend fun getRestaurant(id: RestaurantId): Restaurant {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                restaurantQueries
                    .selectByIdWithDishes(id)
                    .executeAsList()
                    .toRestaurants()
                    .first()
            }
        }
    }
}