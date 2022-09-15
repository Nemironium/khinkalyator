package me.nemiron.khinkalyator.features.people.domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.phone.domain.Phone

interface PeopleStorage {
    fun observeActivePeople(): Flow<List<Person>>

    suspend fun deletePerson(id: PersonId)

    suspend fun addPerson(
        name: String,
        phone: Phone?,
        emoji: Emoji,
        status: Person.Status = Person.Status.Active
    ): PersonId

    suspend fun updatePerson(
        id: PersonId,
        newName: String,
        newPhone: Phone?
    )

    suspend fun getPerson(id: PersonId): Person?
}