package me.nemiron.khinkalyator.features.people.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId

data class PersonViewData(
    val personId: PersonId,
    val title: LocalizedString,
    val isSelected: Boolean
)

fun Person.toPersonViewData(isSelected: Boolean = false): PersonViewData {
    return PersonViewData(
        personId = id,
        title = LocalizedString.raw(name),
        isSelected = isSelected
    )
}