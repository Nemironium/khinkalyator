package me.nemiron.khinkalyator.features.dishes.domain

import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.common_domain.model.Price
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