package me.nemiron.khinkalyator.features.meets.create.ui

import me.nemiron.khinkalyator.features.meets.meet.domain.MeetId
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId

interface CreateMeetComponent {

    val personComponent: PersonComponent?

    val restaurantsViewData: List<RestaurantSimpleViewData>

    val personsViewData: List<PersonSimpleViewData>

    val buttonState: KhinkaliButtonState

    fun onPersonDismissed()

    fun onRestaurantAddClick()

    fun onRestaurantClick(restaurantId: RestaurantId)

    fun onPersonAddClick()

    fun onPersonClick(personId: PersonId)

    fun onCreateMeetClick()

    sealed interface Output {
        object NewRestaurantRequested : Output
        class MeetCreated(val meetId: MeetId) : Output
    }
}