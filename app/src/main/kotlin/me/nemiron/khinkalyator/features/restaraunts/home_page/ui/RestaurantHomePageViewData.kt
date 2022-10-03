package me.nemiron.khinkalyator.features.restaraunts.home_page.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

data class RestaurantHomePageViewData(
    val id: RestaurantId,
    val title: LocalizedString,
    val subtitle: LocalizedString,
    val phoneIconVisible: Boolean
)

fun Restaurant.toRestaurantHomePageViewData(): RestaurantHomePageViewData {
    return RestaurantHomePageViewData(
        id = id,
        title = LocalizedString.raw(name),
        subtitle = LocalizedString.raw(address?.value.orEmpty()),
        phoneIconVisible = phone != null
    )
}