package me.nemiron.khinkalyator.features.people.home_page.ui

import me.nemiron.khinkalyator.features.people.domain.PersonId

interface PeoplePageComponent {

    val peopleViewData: List<PersonHomePageViewData>

    fun onPersonAddClick()

    fun onPersonDeleteClick(personId: PersonId)

    fun onPersonClick(personId: PersonId)

    sealed interface Output {
        object NewPersonRequested : Output
        class PersonRequested(val personId: PersonId) : Output
    }
}