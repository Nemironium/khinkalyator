package me.nemiron.khinkalyator.features.meets.session.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.meets.domain.MeetDish
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.features.meets.domain.onlyNotEmptyOrders
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.ui.PersonViewData
import me.nemiron.khinkalyator.features.people.ui.toMeetPersonViewData

data class MeetSessionDishViewData(
    val dishId: DishId,
    val name: LocalizedString,
    val price: LocalizedString,
    val people: List<PersonViewData>
)

fun MeetSession.toMeetSessionDishViewData(dishId: DishId): MeetSessionDishViewData {
    val dish = dishes.first { it.id == dishId }
    return toMeetSessionDishViewData(dish)
}

fun MeetSession.toMeetSessionDishesViewData(): List<MeetSessionDishViewData> {
    return dishes.map(::toMeetSessionDishViewData)
}

fun MeetSession.toMeetSessionDishViewData(dish: Dish): MeetSessionDishViewData {
    val priceFormatted = dish.price.formatted()

    val peopleInOrder = orders
        .filter { order ->
            order.value
                .onlyNotEmptyOrders()
                .map(MeetDish::dish)
                .contains(dish)
        }
        .keys
        .map(Person::toMeetPersonViewData)

    return MeetSessionDishViewData(
        dishId = dish.id,
        name = LocalizedString.raw(dish.name),
        price = LocalizedString.resource(
            R.string.common_amount_with_currency,
            priceFormatted,
            LocalizedString.resource(R.string.common_currency_symbol)
        ),
        people = peopleInOrder
    )
}