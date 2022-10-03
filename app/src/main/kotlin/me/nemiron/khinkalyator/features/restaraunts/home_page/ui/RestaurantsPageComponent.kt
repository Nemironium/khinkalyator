package me.nemiron.khinkalyator.features.restaraunts.home_page.ui

import me.nemiron.khinkalyator.common_domain.model.RestaurantId

interface RestaurantsPageComponent {

    val restaurantsViewData: List<RestaurantHomePageViewData>

    fun onRestaurantAddClick()

    fun onRestaurantClick(restaurantId: RestaurantId)

    fun onRestaurantCallClick(restaurantId: RestaurantId)

    fun onRestaurantShareClick(restaurantId: RestaurantId)

    sealed interface Output {
        object NewRestaurantRequested : Output
        class RestaurantRequested(val restaurantId: RestaurantId) : Output
    }
}