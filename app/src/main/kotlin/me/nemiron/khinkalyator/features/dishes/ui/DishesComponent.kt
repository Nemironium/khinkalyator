package me.nemiron.khinkalyator.features.dishes.ui

import me.nemiron.khinkalyator.features.dishes.domain.DishId

interface DishesComponent {
    val dishesViewData: List<DishViewData>
    fun onDishAddClick()
    fun onDishClick(dishId: DishId)
}