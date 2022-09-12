package me.nemiron.khinkalyator.features.meets.meet_session_pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.features.meets.domain.MeetDish
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.dishes.domain.toPrice
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.features.meets.domain.onlyNotEmptyOrders
import me.nemiron.khinkalyator.features.meets.domain.sum
import me.nemiron.khinkalyator.features.people.domain.Person

data class MeetSessionPersonViewData(
    val personId: PersonId,
    val initials: InitialsViewData,
    val name: LocalizedString,
    val overallSum: LocalizedString,
    val dishes: List<MeetDishViewData>
)

fun MeetSession.toMeetSessionPersonViewData(personId: PersonId): MeetSessionPersonViewData {
    val person = people.first { it.id == personId }
    return toMeetSessionPersonViewData(person)
}

fun MeetSession.toMeetSessionPeopleViewData(): List<MeetSessionPersonViewData> {
    return people.map(::toMeetSessionPersonViewData)
}

fun MeetSession.toMeetSessionPersonViewData(person: Person): MeetSessionPersonViewData {
    val meetDishes = orders[person]
        .orEmpty()
        .onlyNotEmptyOrders()

    val sumFormatted = meetDishes
        .sum()
        .toPrice()
        .formatted()

    return MeetSessionPersonViewData(
        personId = person.id,
        initials = person.toInitialsViewData(),
        name = LocalizedString.raw(person.name),
        overallSum = LocalizedString.resource(
            R.string.common_amount_with_currency,
            sumFormatted,
            LocalizedString.resource(R.string.common_currency_symbol)
        ),
        dishes = meetDishes.map(MeetDish::toMeetDishViewData)
    )
}