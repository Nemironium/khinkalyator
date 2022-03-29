package me.nemiron.khinkalyator.features.people.domain

import kotlinx.coroutines.flow.Flow

class ObservePersonUseCase(
    private val peopleStorage: PeopleStorage
) {
    operator fun invoke(id: PersonId): Flow<Person?> {
        return peopleStorage.observePerson(id)
    }
}