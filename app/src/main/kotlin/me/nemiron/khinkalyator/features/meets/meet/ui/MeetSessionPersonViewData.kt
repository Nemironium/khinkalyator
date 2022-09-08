package me.nemiron.khinkalyator.features.meets.meet.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetDish
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetSession
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Price
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.toPrice
import java.text.DecimalFormat

data class MeetSessionPersonViewData(
    val personId: PersonId,
    val initials: InitialsViewData,
    val name: LocalizedString,
    val overallSum: LocalizedString,
    val dishes: List<MeetDishViewData>
)

fun MeetSession.toMeetSessionPersonViewData(personId: PersonId): MeetSessionPersonViewData {
    val person = persons.first { it.id == personId }
    val meetDishes = orders[person].orEmpty()
    val sumFormatted = meetDishes
        .sumOf { it.dish.price.value * it.quantity.count }
        .toPrice()
        .formatted()

    return MeetSessionPersonViewData(
        personId = personId,
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

private const val PricePattern = "###,###.##"

// FIXME: move to another package
fun Price.formatted(): String {
    return DecimalFormat(PricePattern)
        .format(value)
        .replace(",", ", ")
}