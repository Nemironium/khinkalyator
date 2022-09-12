package me.nemiron.khinkalyator.features.dishes.domain

import kotlin.random.Random

class CreateDishUseCase {

    operator fun invoke(name: String, price: Double, previousDishId: DishId?): Dish {
        return Dish(
            id = previousDishId ?: Random.nextLong(),
            name = name,
            price = Price(price)
        )
    }
}