package me.nemiron.khinkalyator.features.restaraunts.menu.domain

import kotlin.random.Random

class CreateDishUseCase {

    operator fun invoke(name: String, price: Double, previousDishId: DishId?): Dish {
        return Dish(
            id = previousDishId ?: Random.nextLong(),
            name = name,
            price = price
        )
    }
}