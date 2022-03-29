package me.nemiron.khinkalyator.features.people.page.ui

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent

interface PeoplePageComponent {

    val personComponent: PersonComponent?

    val closeKeyboardEvents: Flow<Unit>

    val peopleViewData: List<PersonFullViewData>

    fun onPersonAddClick()

    fun onPersonDeleteClick(personId: PersonId)

    fun onPersonClick(personId: PersonId)
}