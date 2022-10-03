package me.nemiron.khinkalyator.common_domain.model

typealias DishId = Long

data class Dish(
    val id: DishId,
    val name: String,
    val price: Price,
    // TODO: for future features
    val discount: Discount? = null,
    val status: Status = Status.Active
) {

    val isDiscounted: Boolean
        get() = discount != null

    sealed interface Discount {
        data class Percent(val value: Int) : Discount
        data class Absolut(val value: Double) : Discount
    }

    enum class Status(val code: Int) {
        Archived(0),
        Active(1)
    }

    companion object {
        val MOCKS = listOf(
            Dish(
                id = 11,
                name = "Хинкали с грибами и сыром",
                price = Price(95.0)
            ),
            Dish(
                id = 12,
                name = "Хинкали с бараниной",
                price = Price(90.0)
            ),
            Dish(
                id = 13,
                name = "Хачапури по-аджарски S",
                price = Price(450.0)
            ),
            Dish(
                id = 14,
                name = "Чай в чайнике",
                price = Price(250.0)
            ),
            Dish(
                id = 21,
                name = "Хинкали со шпинатом и сыром",
                price = Price(125.0)
            ),
            Dish(
                id = 22,
                name = "Хинкали с говядиной и свининой",
                price = Price(85.0)
            ),
            Dish(
                id = 23,
                name = "Хачапури по-аджарски M",
                price = Price(550.0)
            ),
            Dish(
                id = 31,
                name = "Хинкали с бараниной",
                price = Price(50.0)
            ),
            Dish(
                id = 32,
                name = "Хинкали с сыром",
                price = Price(55.0)
            ),
            Dish(
                id = 33,
                name = "Хинкали с говядиной и свининой",
                price = Price(45.0)
            ),
            Dish(
                id = 34,
                name = "Хачапури по-мегрельски",
                price = Price(450.0)
            )
        )
    }
}

@JvmInline
value class Price(val value: Double)

fun Double.toPrice(): Price = Price(this)