package me.nemiron.khinkalyator.features.meets.create.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId

data class RestaurantSimpleViewData(
    val id: RestaurantId,
    val title: LocalizedString,
    val isSelected: Boolean
)

fun Restaurant.toRestaurantSimpleViewData(isSelected: Boolean): RestaurantSimpleViewData {
    return RestaurantSimpleViewData(
        id = id,
        title = LocalizedString.raw(name),
        isSelected = isSelected
    )
}
