package me.nemiron.khinkalyator.features.meets.create.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId

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