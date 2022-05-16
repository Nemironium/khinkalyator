package me.nemiron.khinkalyator.features.dish.domain

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

typealias DishId = Long

// FIXME: @Parcelize should not be in domain layer
@Parcelize
data class Dish(
    val id: DishId,
    val name: String,
    val price: Double,
    val discount: Discount? = null
) : Parcelable {

    val isDiscounted: Boolean
        get() = discount != null

    sealed interface Discount : Parcelable {
        @Parcelize
        data class Percent(val value: Int) : Discount

        @Parcelize
        data class Absolut(val value: Double) : Discount
    }
}