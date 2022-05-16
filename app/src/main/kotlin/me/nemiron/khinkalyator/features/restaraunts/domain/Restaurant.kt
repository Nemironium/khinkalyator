package me.nemiron.khinkalyator.features.restaraunts.domain

import me.nemiron.khinkalyator.features.dish.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

typealias RestaurantId = Long

data class Restaurant(
    val id: RestaurantId,
    val name: String,
    val address: Address?,
    val phone: Phone?,
    val menu: List<Dish>
)

@JvmInline
value class Address(val value: String)