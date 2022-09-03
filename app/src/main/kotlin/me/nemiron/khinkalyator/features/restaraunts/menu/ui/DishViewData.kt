package me.nemiron.khinkalyator.features.restaraunts.menu.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId

data class DishViewData(
    val id: DishId,
    val name: LocalizedString,
    val isSelected: Boolean
)

fun Dish.toDishViewData(isSelected: Boolean = false) : DishViewData {
    return DishViewData(
        id = id,
        name = LocalizedString.raw(name),
        isSelected = isSelected
    )
}