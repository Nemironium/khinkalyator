package me.nemiron.khinkalyator.features.restaraunts.menu.domain

typealias DishId = Long

data class Dish(
    val id: DishId,
    val name: String,
    val price: Price,
    // TODO: for future features
    val discount: Discount? = null
) {

    val isDiscounted: Boolean
        get() = discount != null

    sealed interface Discount {
        data class Percent(val value: Int) : Discount
        data class Absolut(val value: Double) : Discount
    }
}

@JvmInline
value class Price(val value: Double)

fun Double.toPrice(): Price = Price(this)