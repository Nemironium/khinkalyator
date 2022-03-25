package me.nemiron.khinkalyator.features.restaraunts.page.ui

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.domain.Restaurant
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantId
import kotlin.random.Random

class RealRestaurantsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (RestaurantsPageComponent.Output) -> Unit
) : RestaurantsPageComponent, ComponentContext by componentContext {

    private val mockedRestaurants = listOf(
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("ул. Рубинштейна, 24"),
            phone = Phone("89219650524"),
            menu = emptyList()
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("Большой проспект П.С., 82"),
            phone = null,
            menu = emptyList()
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Пхали-хинкали",
            address = Address("Большая Морская ул., 27"),
            phone = Phone("89219650524"),
            menu = emptyList()
        )
    )

    override val restaurantsViewData = mockedRestaurants.map(Restaurant::toRestaurantFullViewData)

    override fun onRestaurantAddClick() {
        onOutput(RestaurantsPageComponent.Output.NewRestaurantRequested)
    }

    override fun onRestaurantClick(restaurantId: RestaurantId) {
        onOutput(RestaurantsPageComponent.Output.RestaurantRequested(restaurantId))
    }

    override fun onRestaurantCallClick(restaurantId: RestaurantId) {
        mockedRestaurants.find { it.id == restaurantId }?.phone?.let {
            // TODO: call to ExternalAppService
        }
    }

    override fun onRestaurantShareClick(restaurantId: RestaurantId) {
        mockedRestaurants.find { it.id == restaurantId }?.let {
            // TODO: call to ExternalAppService
        }
    }
}