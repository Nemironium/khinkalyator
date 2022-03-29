package me.nemiron.khinkalyator.features.people.domain

class GetPersonByIdUseCase(
    private val peopleStorage: PeopleStorage
) {
    suspend operator fun invoke(id: PersonId): Person? {
        return peopleStorage.getPerson(id)
    }
}