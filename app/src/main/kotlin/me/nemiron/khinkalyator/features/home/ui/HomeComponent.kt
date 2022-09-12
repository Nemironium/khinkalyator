package me.nemiron.khinkalyator.features.home.ui

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.home_page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.people.home_page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.home_page.ui.RestaurantsPageComponent

interface HomeComponent {

    val meetsPageComponent: MeetsPageComponent
    val restaurantsPageComponent: RestaurantsPageComponent
    val peoplePageComponent: PeoplePageComponent
    val personComponent: PersonComponent?

    val closeKeyboardEvents: Flow<Unit>

    fun onPersonDismissed()

    sealed interface Output {
        object NewMeetRequested : Output
        class MeetRequested(val meetId: MeetId) : Output
        object NewRestaurantRequested : Output
        class RestaurantRequested(val restaurantId: RestaurantId) : Output
    }
}