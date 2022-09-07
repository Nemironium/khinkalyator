package me.nemiron.khinkalyator.features.meets.create.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId

data class PersonSimpleViewData(
    val id: PersonId,
    val emoji: Emoji,
    val title: LocalizedString,
    val isSelected: Boolean
)

fun Person.toPersonSimpleViewData(isSelected: Boolean): PersonSimpleViewData {
    return PersonSimpleViewData(
        id = id,
        emoji = emoji,
        title = LocalizedString.raw(name),
        isSelected = isSelected
    )
}