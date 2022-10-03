package me.nemiron.khinkalyator.common_data.mapper

import me.nemiron.khinkalyator.common_domain.model.Address
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.Price
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId
import menemironkhinkalyator.data.db.RestaurantWithDishesEntity

internal fun Dish.Discount?.getTypeAndValue(): Pair<Int, Double> {
    return when(this) {
        is Dish.Discount.Absolut -> 1 to value
        is Dish.Discount.Percent -> 2 to value.toDouble()
        null -> 0 to 0.0
    }
}

internal fun List<RestaurantWithDishesEntity>.toRestaurants(): List<Restaurant> {
    return groupBy { it.restaurantId }
        .map { (_, selectRows) ->
            val rowData = selectRows.first()
            mapToRestaurant(
                id = rowData.restaurantId,
                name = rowData.restaurantName,
                addressValue = rowData.address,
                phoneValue = rowData.phone,
                type = rowData.restaurantType,
                dishes = selectRows.map {
                    mapToDish(it.dishId, it.dishName, it.price, it.dishType)
                }
            )
        }
}

internal fun mapToDish(
    id: DishId,
    name: String,
    priceValue: Double,
    type: Int
): Dish {
    val status = when (type) {
        0 -> Dish.Status.Archived
        1 -> Dish.Status.Active
        else -> throw IllegalArgumentException("Not supported Dish type")
    }
    return Dish(id, name, Price(priceValue), null, status)
}

private fun mapToRestaurant(
    id: RestaurantId,
    name: String,
    addressValue: String?,
    phoneValue: String?,
    type: Int,
    dishes: List<Dish>
): Restaurant {
    val status = when (type) {
        0 -> Restaurant.Status.Archived
        1 -> Restaurant.Status.Active
        else -> throw IllegalArgumentException("Not supported Restaurant type")
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