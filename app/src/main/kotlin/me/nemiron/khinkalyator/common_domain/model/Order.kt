package me.nemiron.khinkalyator.common_domain.model

typealias OrderId = Long

data class Order(
    val id: OrderId,
    val dish: Dish,
    val quantity: DishQuantity
) {
    sealed interface DishQuantity {
        val count: Int

        data class Individual(override val count: Int) : DishQuantity

        data class Shared(override val count: Int, val people: List<Person>) : DishQuantity
    }
}

fun List<Order>.onlyNotEmptyOrders(): List<Order> {
    return filter { it.quantity.count > 0 }
}

fun List<Order>.sum(): Double = sumOf { meetDish ->
    val count = when (val quantity = meetDish.quantity) {
        is Order.DishQuantity.Individual -> quantity.count.toDouble()
        is Order.DishQuantity.Shared -> (quantity.count.toDouble() / (quantity.people.size + 1))
    }
    meetDish.dish.price.value * count
}