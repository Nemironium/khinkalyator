package me.nemiron.khinkalyator.common_domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

interface MeetsStorage {
    fun observeMeets(): Flow<List<Meet>>

    fun observeMeet(meetId: MeetId): Flow<Meet>

    suspend fun createMeet(
        restaurantId: RestaurantId,
        personIds: List<PersonId>,
        status: Meet.Status
    ): MeetId

    suspend fun getMeet(meetId: MeetId): Meet

    suspend fun addOrder(meetId: MeetId, personId: PersonId, order: Order)

    suspend fun archiveMeet(meetId: MeetId, tips: Meet.Tips?)

    suspend fun deleteMeet(meetId: MeetId)

    suspend fun isPersonAttemptsInAnyMeet(personId: PersonId): Boolean

    suspend fun isRestaurantAttemptsInAnyMeet(restaurantId: RestaurantId): Boolean
}