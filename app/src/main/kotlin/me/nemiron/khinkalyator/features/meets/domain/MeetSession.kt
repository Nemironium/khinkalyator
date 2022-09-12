package me.nemiron.khinkalyator.features.meets.domain

import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.dishes.domain.Price
import me.nemiron.khinkalyator.features.dishes.domain.toPrice
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant

data class MeetSession(
    val id: MeetId,
    val type: Type,
    val restaurant: Restaurant,
    val orders: Map<Person, List<MeetDish>>,
    val tips: Tips? = null
) {
    val people: List<Person> = orders.keys.toList()

    val dishes: List<Dish> = restaurant.dishes

    val overallSum: Price
        get() = orders.map { (_, order) ->
            order.sum()
        }
            .sum()
            .toPrice()

    sealed interface Tips {
        data class Percent(val value: Int) : Tips
        data class Fixed(val price: Price) : Tips
    }

    enum class Type {
        Active, Completed
    }

    companion object {
        val MOCKS = Meet.MOCKS.map { meet ->
            val restaurant = meet.restaurant
            val people = meet.people

            MeetSession(
                id = meet.id,
                type = meet.type.toMeetSessionType(),
                restaurant = restaurant,
                orders = people.associateWith {
                    val count = (0..3).random()
                    val quantity = if (count > 1) {
                        val sharedPersons = people.shuffled().subList(0, 2)
                        MeetDish.DishQuantity.Shared(count, sharedPersons)
                    } else {
                        MeetDish.DishQuantity.Individual(count)
                    }
                    restaurant.dishes.map {
                        MeetDish(it, quantity)
                    }
                }
            )
        }
    }
}

fun List<MeetDish>.onlyNotEmptyOrders(): List<MeetDish> {
    return filter { it.quantity.count > 0 }
}

fun List<MeetDish>.sum(): Double = sumOf { meetDish ->
    meetDish.dish.price.value * meetDish.quantity.count
}

fun Meet.Type.toMeetSessionType(): MeetSession.Type {
    return when (this) {
        is Meet.Type.Active -> MeetSession.Type.Active
        is Meet.Type.Archived -> MeetSession.Type.Completed
    }
}