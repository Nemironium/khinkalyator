package me.nemiron.khinkalyator.features.home.ui

import me.nemiron.khinkalyator.features.meets.ui.MeetsComponent
import me.nemiron.khinkalyator.features.people.page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent

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