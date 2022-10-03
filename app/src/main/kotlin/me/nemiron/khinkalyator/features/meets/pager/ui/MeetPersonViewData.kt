package me.nemiron.khinkalyator.features.meets.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.toPrice
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.common_domain.model.onlyNotEmptyOrders
import me.nemiron.khinkalyator.common_domain.model.sum
import me.nemiron.khinkalyator.common_domain.model.Person

data class MeetPersonViewData(
    val personId: PersonId,
    val initials: InitialsViewData,
    val name: LocalizedString,
    val overallSum: LocalizedString,
    val dishes: List<OrderViewData>
)

fun Meet.toMeetPersonViewData(personId: PersonId): MeetPersonViewData {
    val person = people.first { it.id == personId }
    return toMeetPersonViewData(person)
}

fun Meet.toMeetPeopleViewData(): List<MeetPersonViewData> {
    return people.map(::toMeetPersonViewData)
}

fun Meet.toMeetPersonViewData(person: Person): MeetPersonViewData {
    val meetDishes = orders[person]
        .orEmpty()
        /*.onlyNotEmptyOrders()*/

    val sumFormatted = meetDishes
        .sum()
        .toPrice()
        .formatted()

    return MeetPersonViewData(
        personId = person.id,
        initials = person.toInitialsViewData(),
        name = LocalizedString.raw(person.name),
        overallSum = LocalizedString.resource(
            R.string.common_amount_with_currency,
            sumFormatted,
            LocalizedString.resource(R.string.common_currency_symbol)
        ),
        dishes = meetDishes.map(Order::toOrderViewData)
    )
}