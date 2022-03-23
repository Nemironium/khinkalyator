package me.nemiron.khinkalyator.people.page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.people.domain.Person
import me.nemiron.khinkalyator.people.domain.PersonId
import me.nemiron.khinkalyator.phone.utils.format

data class PersonFullViewData(
    val id: PersonId,
    val name: LocalizedString,
    val phone: LocalizedString?,
    val emoji: LocalizedString
)

fun Person.toPersonFullViewData(): PersonFullViewData {
    return PersonFullViewData(
        id = id,
        name = LocalizedString.raw(name),
        phone = phone?.let { LocalizedString.raw(phone.format()) },
        emoji = LocalizedString.raw(emoji.value)
    )
}
