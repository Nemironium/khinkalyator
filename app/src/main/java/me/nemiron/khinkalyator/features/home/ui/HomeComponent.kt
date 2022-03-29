package me.nemiron.khinkalyator.features.home.ui

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.people.page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent

interface HomeComponent {

    val meetsPageComponent: MeetsPageComponent
    val restaurantsPageComponent: RestaurantsPageComponent
    val peoplePageComponent: PeoplePageComponent
    val personComponent: PersonComponent?

    val closeKeyboardEvents: Flow<Unit>

    enum class Page {
        Meets, Restaurants, People
    }

    sealed interface Output {
        object NewMeetRequested : Output
        object NewRestaurantRequested : Output
    }
}