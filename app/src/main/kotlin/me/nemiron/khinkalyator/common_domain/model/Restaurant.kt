package me.nemiron.khinkalyator.common_domain.model

typealias RestaurantId = Long

data class Restaurant(
    val id: RestaurantId,
    val name: String,
    val address: Address?,
    val phone: Phone?,
    val dishes: List<Dish>,
    val status: Status = Status.Active
) {
    enum class Status(val code: Int) {
        Archived(0),
        Active(1)
    }

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