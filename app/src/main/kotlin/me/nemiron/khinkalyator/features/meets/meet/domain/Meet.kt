package me.nemiron.khinkalyator.features.meets.meet.domain

import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant

typealias MeetId = Long

// TODO: add person dishes for meet
data class Meet(
    val id: MeetId,
    val type: Type,
    val restaurant: Restaurant,
    val persons: List<Person>
) {
    sealed interface Type {
        val createTime: LocalDateTime

        data class Active(override val createTime: LocalDateTime) : Type
        data class Archived(override val createTime: LocalDateTime) : Type
    }
}