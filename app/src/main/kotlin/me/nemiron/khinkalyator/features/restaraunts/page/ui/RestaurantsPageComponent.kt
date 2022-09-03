package me.nemiron.khinkalyator.features.restaraunts.page.ui

import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId

interface RestaurantsPageComponent {

    val restaurantsViewData: List<RestaurantFullViewData>

    fun onRestaurantAddClick()

    fun onRestaurantClick(restaurantId: RestaurantId)

    fun onRestaurantCallClick(restaurantId: RestaurantId)

    fun onRestaurantShareClick(restaurantId: RestaurantId)

    sealed interface Output {
        object NewRestaurantRequested : Output
        class RestaurantRequested(val restaurantId: RestaurantId) : Output
    }
}