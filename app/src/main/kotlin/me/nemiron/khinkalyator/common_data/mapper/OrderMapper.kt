package me.nemiron.khinkalyator.common_data.mapper

import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.OrderId
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId

internal data class MeetOrder(
    val orderId: OrderId,
    val dish: Dish,
    val person: Person,
    val count: Int
)

// формируем заказы пользователей по каждому доступному заказу
/**
 * Map all [Order]s by [PersonId]
 */
internal fun getPeopleOrders(
    meetOrders: List<MeetOrder>
): Map<Person, List<Order>> {
    val personOrders = mutableMapOf<Person, List<Order>>()

    meetOrders
        .groupBy { it.orderId }
        .map { (_, meetOrders) ->
            val orderRow = meetOrders.first()

            if (meetOrders.size == 1) {
                val person = orderRow.person

                val order = Order(
                    id = orderRow.orderId,
                    dish = orderRow.dish,
                    quantity = Order.DishQuantity.Individual(orderRow.count)
                )
                val otherOrders = personOrders.getOrDefault(person, emptyList())
                personOrders[person] = otherOrders + order
            } else {
                val sharedPeople = meetOrders.map { it.person }
                meetOrders.forEach { personRow ->
                    val person = personRow.person

                    val order = Order(
                        id = personRow.orderId,
                        dish = personRow.dish,
                        quantity = Order.DishQuantity.Shared(personRow.count, sharedPeople - person)
                    )
                    val otherOrders = personOrders.getOrDefault(person, emptyList())
                    personOrders[person] = otherOrders + order
                }
            }
        }

    return personOrders
}