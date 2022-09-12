package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import kotlin.time.Duration.Companion.days

typealias MeetId = Long

data class Meet(
    val id: MeetId,
    val type: Type,
    val restaurant: Restaurant,
    val people: List<Person>
) {
    sealed interface Type {
        val createTime: LocalDateTime

        data class Active(override val createTime: LocalDateTime) : Type
        data class Archived(override val createTime: LocalDateTime) : Type
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
                    type = Type.Active(todayDate),
                    restaurant = Restaurant.MOCKS.component1(),
                    people = Person.MOCKS.shuffled()
                )
            )

            add(
                Meet(
                    id = 2,
                    type = Type.Archived(todayDate),
                    restaurant = Restaurant.MOCKS.component2(),
                    people = Person.MOCKS.shuffled().take(3)
                )
            )

            add(
                Meet(
                    id = 3,
                    type = Type.Archived(weekAgoDate),
                    restaurant = Restaurant.MOCKS.component3(),
                    people = Person.MOCKS.shuffled().take(4)
                )
            )
        }
    }
}