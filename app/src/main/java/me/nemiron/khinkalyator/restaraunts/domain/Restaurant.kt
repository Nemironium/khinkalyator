package me.nemiron.khinkalyator.restaraunts.domain

typealias RestaurantId = Long

data class Restaurant(
    val id: RestaurantId,
    val name: String,
    val address: Address
)

@JvmInline
value class Address(val value: String)
