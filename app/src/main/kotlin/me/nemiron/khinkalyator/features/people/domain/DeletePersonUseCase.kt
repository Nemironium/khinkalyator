package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.PeopleStorage
import me.nemiron.khinkalyator.common_domain.exception.PersonInActiveMeetException
import me.nemiron.khinkalyator.common_domain.model.PersonId

class DeletePersonUseCase(
    private val peopleStorage: PeopleStorage,
    private val meetsStorage: MeetsStorage
) {
    suspend operator fun invoke(personId: PersonId) {
        val isPersonAttemptsInAnyMeet = meetsStorage.isPersonAttemptsInAnyMeet(personId)
        if (isPersonAttemptsInAnyMeet) {
            throw PersonInActiveMeetException
        } else {
            peopleStorage.deletePerson(personId)
        }
    }
}