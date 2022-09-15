package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.features.phone.domain.Phone

class UpdatePersonUseCase(
    private val peopleStorage: PeopleStorage
) {

    suspend operator fun invoke(personId: PersonId, name: String, phone: String?) {
        peopleStorage.updatePerson(
            id = personId,
            newName = name,
            newPhone = phone?.let(::Phone)
        )
    }
}