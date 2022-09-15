package me.nemiron.khinkalyator.features.people.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveActivePeopleUseCase(
    private val peopleStorage: PeopleStorage
) {
    operator fun invoke(): Flow<List<Person>> {
        return peopleStorage.observeActivePeople()
            .map { peopleList ->
                peopleList.filter { it.status == Person.Status.Active }
            }
    }
}