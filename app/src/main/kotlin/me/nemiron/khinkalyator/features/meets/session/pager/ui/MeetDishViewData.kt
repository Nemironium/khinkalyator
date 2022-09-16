package me.nemiron.khinkalyator.features.meets.session.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.features.meets.domain.MeetDish
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.dishes.domain.DishId

data class MeetDishViewData(
    val dishId: DishId,
    val title: LocalizedString,
    val sharedInitials: List<InitialsViewData>?
)

fun MeetDish.toMeetDishViewData(): MeetDishViewData {
    return MeetDishViewData(
        dishId = dish.id,
        title = LocalizedString.resource(
            R.string.meet_session_pager_name_with_count,
            dish.name,
            quantity.count
        ),
        sharedInitials = when (quantity) {
            is MeetDish.DishQuantity.Individual -> null
            is MeetDish.DishQuantity.Shared -> quantity.people.map(Person::toInitialsViewData)
        }
    )
}