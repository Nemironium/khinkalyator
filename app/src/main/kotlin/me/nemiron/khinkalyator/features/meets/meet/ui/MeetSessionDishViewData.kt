package me.nemiron.khinkalyator.features.meets.meet.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetDish
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetSession
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId

data class MeetSessionDishViewData(
    val dishId: DishId,
    val name: LocalizedString,
    val price: LocalizedString,
    val persons: List<MeetPersonViewData>
)

fun MeetSession.toMeetSessionDishViewData(dishId: DishId): MeetSessionDishViewData {
    val dish = dishes.first { it.id == dishId }
    val priceFormatted = dish.price.formatted()
    val personsInOrder = orders
        .filter { order ->
            order.value.map(MeetDish::dish).contains(dish)
        }
        .keys
        .map { it.toMeetPersonViewData() }

    return MeetSessionDishViewData(
        dishId = dishId,
        name = LocalizedString.raw(dish.name),
        price = LocalizedString.resource(
            R.string.common_amount_with_currency,
            priceFormatted,
            LocalizedString.resource(R.string.common_currency_symbol)
        ),
        persons = personsInOrder
    )
}