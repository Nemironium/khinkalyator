package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.common_domain.PeopleStorage
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId

class GetPersonByIdUseCase(
    private val peopleStorage: PeopleStorage
) {
    suspend operator fun invoke(id: PersonId): Person {
        return peopleStorage.getPerson(id)
    }
}