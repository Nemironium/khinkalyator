package me.nemiron.khinkalyator.features.restaraunts.new.ui

interface NewRestaurantComponent {

    fun onDeleteRestaurantClick()

    sealed interface Output {
        object RestaurantDeleted : Output
    }
}