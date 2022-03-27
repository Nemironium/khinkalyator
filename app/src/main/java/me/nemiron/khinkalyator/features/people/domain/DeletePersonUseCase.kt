package me.nemiron.khinkalyator.features.people.domain

class DeletePersonUseCase(
    private val peopleStorage: PeopleStorage
) {
    suspend operator fun invoke(personId: PersonId) {
        /*
        * TODO: check that person doesn't attempt in any Meet
        *  if true – just delete it from storage
        *  if false – replace it with Person.DELETED with previous id and name
        *  resource(R.string.person_deleted, deletedCount+1),
        *  deletedCount depends on count of already deleted person
        * */
        peopleStorage.deletePerson(personId)
    }
}