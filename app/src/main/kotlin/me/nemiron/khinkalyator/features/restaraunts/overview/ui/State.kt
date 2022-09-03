package me.nemiron.khinkalyator.features.restaraunts.overview.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.MenuDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.toDishViewData
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantDetailsComponent

class State : RestaurantDetailsComponent.State, MenuDetailsComponent.State {
    private var restaurantName: String? by mutableStateOf(null)
    private var restaurantAddress: Address? by mutableStateOf(null)
    private var restaurantPhone: Phone? by mutableStateOf(null)
    private var menu: Map<DishId, Dish> by mutableStateOf(emptyMap())

    override val name by derivedStateOf { restaurantName }
    override val address by derivedStateOf { restaurantAddress }
    override val phone by derivedStateOf { restaurantPhone }
    override val dishes by derivedStateOf { menu.values.toList() }

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
        menu = menu.toMutableMap().apply {
            remove(dish.id, dish)
        }
    }

    fun setDishes(dishes: List<Dish>) {
        menu = dishes.associateBy(Dish::id)
    }

    private fun addOrEditDish(dish: Dish) {
        menu = menu.toMutableMap().apply {
            put(dish.id, dish)
        }
    }
}