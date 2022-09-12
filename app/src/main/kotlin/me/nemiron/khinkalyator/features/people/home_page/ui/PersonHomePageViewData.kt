package me.nemiron.khinkalyator.features.people.home_page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.phone.utils.format

data class PersonHomePageViewData(
    val id: PersonId,
    val name: LocalizedString,
    val phone: LocalizedString?,
    val emoji: Emoji
)

fun Person.toPersonHomePageViewData(): PersonHomePageViewData {
    return PersonHomePageViewData(
        id = id,
        name = LocalizedString.raw(name),
        phone = phone?.let { LocalizedString.raw(phone.format()) },
        emoji = emoji
    )
}
