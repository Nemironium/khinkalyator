package me.nemiron.khinkalyator.features.dishes.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.domain.Price
import java.text.DecimalFormat

data class DishViewData(
    val id: DishId,
    val title: LocalizedString,
    val isSelected: Boolean
)

fun Dish.toDishViewData(isSelected: Boolean = false): DishViewData {
    return DishViewData(
        id = id,
        title = LocalizedString.raw(name),
        isSelected = isSelected
    )
}

private const val PricePattern = "###,###.##"

fun Price.formatted(): String {
    return DecimalFormat(PricePattern)
        .format(value)
        .replace(",", ", ")
}