package me.nemiron.khinkalyator.features.meets.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.DishId

data class OrderViewData(
    val dishId: DishId,
    val title: LocalizedString,
    val sharedInitials: List<InitialsViewData>?
)

fun Order.toOrderViewData(): OrderViewData {
    return OrderViewData(
        dishId = dish.id,
        title = LocalizedString.resource(
            R.string.meet_pager_name_with_count,
            dish.name,
            quantity.count
        ),
        sharedInitials = when (quantity) {
            is Order.DishQuantity.Individual -> null
            is Order.DishQuantity.Shared -> quantity.people.map(Person::toInitialsViewData)
        }
    )
}
