package me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui.RestaurantDishesComponent
import me.nemiron.khinkalyator.features.dishes.ui.toDishViewData
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.details.ui.RestaurantDetailsComponent

class RestaurantState : RestaurantDetailsComponent.State, RestaurantDishesComponent.State {
    private var restaurantName: String? by mutableStateOf(null)
    private var restaurantAddress: Address? by mutableStateOf(null)
    private var restaurantPhone: Phone? by mutableStateOf(null)
    private var innerDishes: Map<DishId, Dish> by mutableStateOf(emptyMap())

    override val name by derivedStateOf { restaurantName }
    override val address by derivedStateOf { restaurantAddress }
    override val phone by derivedStateOf { restaurantPhone }
    override val dishes by derivedStateOf { innerDishes.values.toList() }

    override val dishesViewData by derivedStateOf {
        dishes.map(Dish::toDishViewData)
    }

    override fun setName(name: String) {
        restaurantName = name
    }

    override fun setAddress(address: Address) {
        restaurantAddress = address
    }

    override fun setPhone(phone: Phone) {
        restaurantPhone = phone
    }

    override fun addDish(dish: Dish) {
        addOrEditDish(dish)
    }

    override fun editDish(dish: Dish) {
        addOrEditDish(dish)
    }

    override fun deleteDish(dish: Dish) {
        innerDishes = innerDishes.toMutableMap().apply {
            remove(dish.id, dish)
        }
    }

    fun setDishes(dishes: List<Dish>) {
        innerDishes = dishes.associateBy(Dish::id)
    }

    private fun addOrEditDish(dish: Dish) {
        innerDishes = innerDishes.toMutableMap().apply {
            put(dish.id, dish)
        }
    }
}