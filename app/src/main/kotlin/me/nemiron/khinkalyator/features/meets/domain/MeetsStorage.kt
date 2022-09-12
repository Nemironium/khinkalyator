package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant

interface MeetsStorage {
    fun observeMeets(): Flow<List<Meet>>

    fun observeMeetSession(meetId: MeetId): Flow<MeetSession?>

    suspend fun createMeet(
        restaurant: Restaurant,
        people: List<Person>,
        createDate: LocalDateTime
    ): MeetId

    suspend fun getMeet(id: MeetId): Meet?
}