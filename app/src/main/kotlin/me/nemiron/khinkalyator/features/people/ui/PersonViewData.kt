package me.nemiron.khinkalyator.features.people.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId

data class PersonViewData(
    val personId: PersonId,
    val title: LocalizedString,
    val isSelected: Boolean
)

fun Person.toMeetPersonViewData(isSelected: Boolean = false): PersonViewData {
    return PersonViewData(
        personId = id,
        title = LocalizedString.raw(name),
        isSelected = isSelected
    )
}