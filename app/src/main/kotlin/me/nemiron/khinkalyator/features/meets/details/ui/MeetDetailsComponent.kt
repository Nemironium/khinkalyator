package me.nemiron.khinkalyator.features.meets.details.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.PersonId

interface MeetDetailsComponent {

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