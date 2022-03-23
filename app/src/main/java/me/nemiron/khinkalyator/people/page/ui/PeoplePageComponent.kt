package me.nemiron.khinkalyator.people.page.ui

import me.nemiron.khinkalyator.people.domain.PersonId

interface PeoplePageComponent {

    val peopleViewData: List<PersonFullViewData>

    fun onPersonAddClick()

    fun onPersonDeleteClick(personId: PersonId)

    fun onPersonClick(personId: PersonId)
}