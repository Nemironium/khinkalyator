package me.nemiron.khinkalyator.home.ui

import me.nemiron.khinkalyator.meets.ui.MeetsComponent
import me.nemiron.khinkalyator.people.page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.restaraunts.page.ui.RestaurantsPageComponent

interface HomeComponent {

    val meetsComponent: MeetsComponent
    val restaurantsPageComponent: RestaurantsPageComponent
    val peoplePageComponent: PeoplePageComponent

    enum class Page {
        Meets, Restaurants, People
    }

    sealed interface Output {
        object NewRestaurantRequested : Output
    }
}