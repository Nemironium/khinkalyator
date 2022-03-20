package me.nemiron.khinkalyator.home.ui

import me.nemiron.khinkalyator.meets.ui.MeetsComponent
import me.nemiron.khinkalyator.people.ui.PeopleComponent
import me.nemiron.khinkalyator.restaraunts.ui.RestaurantsComponent

interface HomeComponent {

    val meetsComponent: MeetsComponent
    val restaurantsComponent: RestaurantsComponent
    val peopleComponent: PeopleComponent

    enum class Page {
        Meets, Restaurants, People
    }

    sealed interface Output
}