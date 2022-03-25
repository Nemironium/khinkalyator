package me.nemiron.khinkalyator.features.people.page.ui

import me.nemiron.khinkalyator.features.people.domain.PersonId

interface PeoplePageComponent {

    val peopleViewData: List<PersonFullViewData>

    fun onPersonAddClick()

    fun onPersonDeleteClick(personId: PersonId)

    fun onPersonClick(personId: PersonId)
}