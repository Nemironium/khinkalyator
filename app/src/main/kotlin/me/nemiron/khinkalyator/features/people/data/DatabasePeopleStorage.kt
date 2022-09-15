package me.nemiron.khinkalyator.features.people.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.phone.domain.Phone
import menemironkhinkalyator.data.db.PersonEntity

class DatabasePeopleStorage(
    db: KhinkalytorDatabase,
    private val coroutineDispatcher: CoroutineDispatcher
) : PeopleStorage {

    private val queries = db.personQueries

    override fun observeActivePeople(): Flow<List<Person>> {
        return queries.selectAllActive()
            .asFlow()
            .mapToList()
            .map { it.toDomain() }
    }

    override suspend fun deletePerson(id: PersonId) {
        withContext(coroutineDispatcher) {
            queries.delete(id)
        }
    }

    override suspend fun addPerson(
        name: String,
        phone: Phone?,
        emoji: Emoji,
        status: Person.Status
    ): PersonId {
        return withContext(coroutineDispatcher) {
            queries.insertOrReplace(
                id = null,
                name = name,
                phone = phone?.value,
                emoji = emoji.value,
                type = status.code
            )
            queries.getLastInsertedId().executeAsOne()
        }
    }

    override suspend fun updatePerson(id: PersonId, newName: String, newPhone: Phone?) {
        withContext(coroutineDispatcher) {
            queries.transaction {
                queries.updateName(newName, id)
                queries.updatePhone(newPhone?.value, id)
            }
        }
    }

    override suspend fun getPerson(id: PersonId): Person? {
        return withContext(coroutineDispatcher) {
            queries
                .selectById(id)
                .executeAsOneOrNull()
                ?.toDomain()
        }
    }

    private fun List<PersonEntity>.toDomain() = map { it.toDomain() }

    private fun PersonEntity.toDomain(): Person {
        val status = when (type) {
            1L -> Person.Status.Active
            else -> Person.Status.Archived
        }
        return Person(id, name, phone?.let(::Phone), Emoji(emoji), status)
    }
}