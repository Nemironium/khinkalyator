package me.nemiron.khinkalyator.common_domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.Phone

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

    suspend fun getPerson(id: PersonId): Person
}