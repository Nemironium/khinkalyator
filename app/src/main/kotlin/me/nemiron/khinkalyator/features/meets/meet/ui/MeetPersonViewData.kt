package me.nemiron.khinkalyator.features.meets.meet.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId

data class MeetPersonViewData(
    val personId: PersonId,
    val name: LocalizedString
)

fun Person.toMeetPersonViewData(): MeetPersonViewData {
    return MeetPersonViewData(id, LocalizedString.raw(name))
}