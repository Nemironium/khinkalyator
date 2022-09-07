package me.nemiron.khinkalyator.features.people.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.phone.domain.Phone
import kotlin.random.Random

class InMemoryPeopleStorage : PeopleStorage {

    private val mockedPeople = listOf(
        Person(
            id = Random.nextLong(),
            name = "Ритуза",
            phone = null,
            emoji = Emoji("🐵")
        ),
        Person(
            id = Random.nextLong(),
            name = "Элина Зайникеева",
            phone = null,
            emoji = Emoji("🐰")
        ),
        Person(
            id = Random.nextLong(),
            name = "Павел Александров",
            phone = Phone("89041930639"),
            emoji = Emoji("🐙")
        ),
        Person(
            id = Random.nextLong(),
            name = "Жека Кауров",
            phone = null,
            emoji = Emoji("🐨")
        ),
        Person(
            id = Random.nextLong(),
            name = "Томочка Тараненко",
            phone = null,
            emoji = Emoji("🦄")
        ),
        Person(
            id = Random.nextLong(),
            name = "Тёма Шанин",
            phone = null,
            emoji = Emoji("🐼")
        ),
        Person(
            id = Random.nextLong(),
            name = "Макс Цекин",
            phone = null,
            emoji = Emoji("🐮")
        ),
        Person(
            id = Random.nextLong(),
            name = "Настя Станкова",
            phone = null,
            emoji = Emoji("🐱")
        )
    ).associateBy { it.id }

    private val stateFlow = MutableStateFlow(mockedPeople)

    private val peopleMutex = Mutex()

    override fun observePeople(): Flow<List<Person>> {
        return stateFlow
            .asStateFlow()
            .map { it.values.toList() }
    }

    override suspend fun deletePerson(id: PersonId) {
        peopleMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply { remove(id) }
        }
    }

    override suspend fun addPerson(name: String, phone: Phone?, emoji: Emoji) {
        peopleMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val id = Random.nextLong()
                    val person = Person(id, name, phone, emoji)
                    put(id, person)
                }
        }
    }

    // TODO: support emoji updating
    override suspend fun updatePerson(
        id: PersonId,
        newName: String,
        newPhone: Phone?
    ) {
        peopleMutex.withLock {
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val previousPerson = get(id)
                    previousPerson?.let {
                        val updatedPerson = previousPerson.copy(
                            name = newName,
                            phone = newPhone
                        )
                        put(id, updatedPerson)
                    }
                }
        }
    }

    override suspend fun getPerson(id: PersonId): Person? {
        return stateFlow
            .asStateFlow()
            .map { peopleMap ->
                peopleMap[id]
            }
            .first()
    }
}