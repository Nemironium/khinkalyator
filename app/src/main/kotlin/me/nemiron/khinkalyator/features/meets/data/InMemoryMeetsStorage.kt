package me.nemiron.khinkalyator.features.meets.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.features.meets.domain.Meet
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.meets.domain.MeetsStorage
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import kotlin.random.Random

class InMemoryMeetsStorage : MeetsStorage {

    private val mockedMeets = Meet.MOCKS.associateBy(Meet::id)

    private val mockedSessions = MeetSession.MOCKS.associateBy(MeetSession::id)

    private val meetsStateFlow = MutableStateFlow(mockedMeets)

    private val sessionsStateFlow = MutableStateFlow(mockedSessions)

    private val meetsMutex = Mutex()

    override fun observeMeets(): Flow<List<Meet>> {
        return meetsStateFlow
            .asStateFlow()
            .map { it.values.toList() }
    }

    override fun observeMeetSession(meetId: MeetId): Flow<MeetSession?> {
        return sessionsStateFlow
            .asStateFlow()
            .map { meetSessionsMap ->
                meetSessionsMap.values.find { session -> session.id == meetId }
            }
    }

    override suspend fun createMeetSession(
        restaurant: Restaurant,
        people: List<Person>,
        createDate: LocalDateTime
    ): MeetId {
        return meetsMutex.withLock {
            val id = Random.nextLong()
            meetsStateFlow.value = meetsStateFlow.value.toMutableMap()
                .apply {
                    val meet = Meet(id, Meet.Type.Active(createDate), restaurant, people)
                    put(id, meet)
                }
            id
        }
    }

    override suspend fun getMeet(id: MeetId): Meet? {
        return meetsStateFlow
            .asStateFlow()
            .map { meetsMap ->
                meetsMap[id]
            }
            .first()
    }

    override suspend fun getMeetSession(id: MeetId): MeetSession? {
        return sessionsStateFlow
            .asStateFlow()
            .map { meetsMap ->
                meetsMap[id]
            }
            .first()
    }
}