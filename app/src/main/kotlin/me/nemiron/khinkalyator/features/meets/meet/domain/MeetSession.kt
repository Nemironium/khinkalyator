package me.nemiron.khinkalyator.features.meets.meet.domain

import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Price
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant

typealias SessionId = Long

data class MeetSession(
    val id: SessionId,
    val restaurant: Restaurant,
    val orders: Map<Person, List<MeetDish>>,
    val tips: Tips? = null
) {
    val persons: List<Person> = orders.keys.toList()

    val dishes: List<Dish> = restaurant.menu

    sealed interface Tips {
        data class Percent(val value: Int) : Tips
        data class Fixed(val price: Price) : Tips
    }
}