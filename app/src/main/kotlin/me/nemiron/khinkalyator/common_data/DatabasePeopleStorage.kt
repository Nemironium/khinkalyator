package me.nemiron.khinkalyator.common_data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.common_data.mapper.mapToPerson
import me.nemiron.khinkalyator.common_data.utils.suspendDbCall
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.common_domain.PeopleStorage
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.Phone

class DatabasePeopleStorage(
    db: KhinkalytorDatabase,
    private val coroutineDispatcher: CoroutineDispatcher
) : PeopleStorage {

    private val queries = db.personQueries

    override fun observeActivePeople(): Flow<List<Person>> {
        return queries
            .selectAllActive(::mapToPerson)
            .asFlow()
            .mapToList()
    }

    override suspend fun deletePerson(id: PersonId) {
        suspendDbCall {
            withContext(coroutineDispatcher) {
                queries.delete(id)
            }
        }
    }

    override suspend fun addPerson(
        name: String,
        phone: Phone?,
        emoji: Emoji,
        status: Person.Status
    ): PersonId {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
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
    }

    override suspend fun updatePerson(id: PersonId, newName: String, newPhone: Phone?) {
        suspendDbCall {
            withContext(coroutineDispatcher) {
                queries.transaction {
                    queries.updateName(newName, id)
                    queries.updatePhone(newPhone?.value, id)
                }
            }
        }
    }

    override suspend fun getPerson(id: PersonId): Person {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                queries
                    .selectById(id, ::mapToPerson)
                    .executeAsOne()
            }
        }
    }
}