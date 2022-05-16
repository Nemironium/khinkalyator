package me.nemiron.khinkalyator.features.dish.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.dish.domain.DishId

data class DishViewData(
    val id: DishId,
    val name: LocalizedString
)

fun Dish.toDishViewData() : DishViewData {
    return DishViewData(
        id = id,
        name = LocalizedString.raw(name)
    )
}