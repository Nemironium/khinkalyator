package me.nemiron.khinkalyator.features.people.page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.phone.utils.format

data class PersonFullViewData(
    val id: PersonId,
    val name: LocalizedString,
    val phone: LocalizedString?,
    val emoji: Emoji
)

fun Person.toPersonFullViewData(): PersonFullViewData {
    return PersonFullViewData(
        id = id,
        name = LocalizedString.raw(name),
        phone = phone?.let { LocalizedString.raw(phone.format()) },
        emoji = emoji
    )
}
