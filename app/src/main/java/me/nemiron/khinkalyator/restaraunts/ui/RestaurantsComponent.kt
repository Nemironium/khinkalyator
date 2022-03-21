package me.nemiron.khinkalyator.restaraunts.ui

interface RestaurantsComponent {

    fun onAddRestaurantClick()

    sealed interface Output {
        object NewRestaurantRequested : Output
    }
}