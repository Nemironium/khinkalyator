package me.nemiron.khinkalyator.features.restaraunts.restaurant.domain

import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.phone.domain.Phone

typealias RestaurantId = Long

data class Restaurant(
    val id: RestaurantId,
    val name: String,
    val address: Address?,
    val phone: Phone?,
    val dishes: List<Dish>
) {
    companion object {
        val MOCKS = listOf(
            Restaurant(
                id = 1,
                name = "Каха бар",
                address = Address("ул. Рубинштейна, 24"),
                phone = Phone("89219650524"),
                dishes = Dish.MOCKS.subList(0, 4)
            ),
            Restaurant(
                id = 2,
                name = "Каха бар",
                address = Address("Большой проспект П.С., 82"),
                phone = null,
                dishes = Dish.MOCKS.subList(4, 7)
            ),
            Restaurant(
                id = 3,
                name = "Пхали-хинкали",
                address = Address("Большая Морская ул., 27"),
                phone = Phone("89219650524"),
                dishes = Dish.MOCKS.subList(7, 11)
            )
        )
    }
}

@JvmInline
value class Address(val value: String)