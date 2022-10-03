package me.nemiron.khinkalyator.features.meets.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.onlyNotEmptyOrders
import me.nemiron.khinkalyator.features.people.ui.PersonViewData
import me.nemiron.khinkalyator.features.people.ui.toPersonViewData

data class MeetDishViewData(
    val dishId: DishId,
    val name: LocalizedString,
    val price: LocalizedString,
    val people: List<PersonViewData>
)

fun Meet.toMeetDishViewData(dishId: DishId): MeetDishViewData {
    val dish = dishes.first { it.id == dishId }
    return toMeetDishViewData(dish)
}

fun Meet.toMeetDishesViewData(): List<MeetDishViewData> {
    return dishes.map(::toMeetDishViewData)
}

fun Meet.toMeetDishViewData(dish: Dish): MeetDishViewData {
    val priceFormatted = dish.price.formatted()

    val peopleInOrder = orders
        .filter { order ->
            order.value
                .onlyNotEmptyOrders()
                .map(Order::dish)
                .contains(dish)
        }
        .keys
        .map(Person::toPersonViewData)

    return MeetDishViewData(
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