package me.nemiron.khinkalyator.features.meets.meet.domain

import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish

data class MeetDish(
    val dish: Dish,
    val quantity: DishQuantity
) {
    sealed interface DishQuantity {
        val count: Int

        data class Individual(override val count: Int) : DishQuantity

        data class Shared(override val count: Int, val persons: List<Person>) : DishQuantity
    }
}