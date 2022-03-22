package me.nemiron.khinkalyator.restaraunts.page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.restaraunts.domain.Restaurant

data class RestaurantFullViewData(
    val title: LocalizedString,
    val subtitle: LocalizedString,
    val phoneIconVisible: Boolean
)

fun Restaurant.toRestaurantFullViewData(): RestaurantFullViewData {
    return RestaurantFullViewData(
        title = LocalizedString.raw(name),
        subtitle = LocalizedString.raw(address?.value.orEmpty()),
        phoneIconVisible = phone != null
    )
}