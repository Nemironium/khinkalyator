package me.nemiron.khinkalyator.common_domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

typealias MeetId = Long

data class Meet(
    val id: MeetId,
    val restaurant: Restaurant,
    val people: List<Person>,
    val orders: Map<Person, List<Order>>,
    val status: Status,
    val tips: Tips? = null
) {

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

    sealed interface Status {
        val createTime: LocalDateTime

        data class Active(override val createTime: LocalDateTime) : Status
        data class Archived(override val createTime: LocalDateTime) : Status

        companion object {
            val Default = Archived(LocalDateTime.parse("1980-01-01T00:00:00"))
        }
    }

    companion object {
        val MOCKS = buildList {
            val currentTimeZone = TimeZone.currentSystemDefault()
            val instantTime = Clock.System.now()
            val todayDate = instantTime.toLocalDateTime(currentTimeZone)
            val weekAgoDate = instantTime.minus(7.days).toLocalDateTime(currentTimeZone)

            add(
                Meet(
                    id = 1,
                    status = Status.Active(todayDate),
                    restaurant = Restaurant.MOCKS.component1(),
                    people = Person.MOCKS,
                    orders = getMockOrders(
                        Person.MOCKS,
                        Restaurant.MOCKS.component1().dishes
                    )
                )
            )

            add(
                Meet(
                    id = 2,
                    status = Status.Archived(todayDate),
                    restaurant = Restaurant.MOCKS.component2(),
                    people = Person.MOCKS.take(3),
                    orders = getMockOrders(
                        Person.MOCKS.take(3),
                        Restaurant.MOCKS.component2().dishes
                    ),
                    tips = Tips.Percent(10)
                )
            )

            add(
                Meet(
                    id = 3,
                    status = Status.Archived(weekAgoDate),
                    restaurant = Restaurant.MOCKS.component3(),
                    people = Person.MOCKS.take(4),
                    orders = getMockOrders(
                        Person.MOCKS.take(4),
                        Restaurant.MOCKS.component3().dishes
                    )
                )
            )
        }

        private fun getMockOrders(
            meetPeople: List<Person>,
            meetDishes: List<Dish>
        ): Map<Person, List<Order>> {
            return meetPeople.associateWith {
                val count = (0..3).random()
                val quantity = if (count > 1) {
                    val sharedPersons = meetPeople.shuffled().subList(0, 2)
                    Order.DishQuantity.Shared(count, sharedPersons)
                } else {
                    Order.DishQuantity.Individual(count)
                }
                meetDishes.map {
                    Order(Random.nextLong(), it, quantity)
                }
            }
        }
    }
}

val Meet.Status.code: Int
    get() = when (this) {
        is Meet.Status.Active -> 1
        is Meet.Status.Archived -> 0
    }