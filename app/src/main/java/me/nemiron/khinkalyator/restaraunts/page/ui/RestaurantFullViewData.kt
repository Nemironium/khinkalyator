package me.nemiron.khinkalyator.restaraunts.page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.restaraunts.domain.Restaurant
import me.nemiron.khinkalyator.restaraunts.domain.RestaurantId

data class RestaurantFullViewData(
    val id: RestaurantId,
    val title: LocalizedString,
    val subtitle: LocalizedString,
    val phoneIconVisible: Boolean
)

fun Restaurant.toRestaurantFullViewData(): RestaurantFullViewData {
    return RestaurantFullViewData(
        id = id,
        title = LocalizedString.raw(name),
        subtitle = LocalizedString.raw(address?.value.orEmpty()),
        phoneIconVisible = phone != null
    )
}