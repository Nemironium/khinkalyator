package me.nemiron.khinkalyator.restaraunts.new.ui

interface NewRestaurantComponent {

    fun onDeleteRestaurantClick()

    sealed interface Output {
        object RestaurantDeleted : Output
    }
}