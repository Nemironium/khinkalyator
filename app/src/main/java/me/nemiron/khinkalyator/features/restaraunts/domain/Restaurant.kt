package me.nemiron.khinkalyator.features.restaraunts.domain

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

data class Dish(
    val name: String,
    val price: Double,
    val discount: Discount? = null
) {

    val isDiscounted: Boolean
        get() = discount != null

    sealed class Discount {
        data class Percent(val value: Int) : Discount()
        data class Absolut(val value: Double) : Discount()
    }
}
