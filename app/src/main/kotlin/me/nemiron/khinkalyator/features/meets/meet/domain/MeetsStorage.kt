package me.nemiron.khinkalyator.features.meets.meet.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant

interface MeetsStorage {
    fun observeMeets(): Flow<List<Meet>>
    suspend fun createMeet(
        restaurant: Restaurant,
        persons: List<Person>,
        createDate: LocalDateTime
    ): MeetId
}