package me.nemiron.khinkalyator.features.dishes.ui

import me.nemiron.khinkalyator.common_domain.model.DishId

interface DishesComponent {
    val dishesViewData: List<DishViewData>
    fun onDishAddClick()
    fun onDishClick(dishId: DishId)
}