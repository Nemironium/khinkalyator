package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.domain.Restaurant

typealias MeetId = Long

// TODO: add person dishes for meet
data class Meet(
    val id: MeetId,
    val type: Type,
    val restaurant: Restaurant,
    val persons: List<Person>
) {
    sealed interface Type {
        object Active : Type
        data class Archived(val dateTime: LocalDateTime) : Type
    }
}