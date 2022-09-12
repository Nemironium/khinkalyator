package me.nemiron.khinkalyator.features.meets.meet_session_details.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.people.domain.PersonId

interface MeetSessionDetailsComponent {

    sealed interface Configuration : Parcelable {
        val meetId: MeetId

        @Parcelize
        class PersonDetails(
            override val meetId: MeetId,
            val personId: PersonId,
            val selectedDishId: DishId?
        ) : Configuration

        @Parcelize
        class DishDetails(override val meetId: MeetId, val dishId: DishId) : Configuration
    }
}