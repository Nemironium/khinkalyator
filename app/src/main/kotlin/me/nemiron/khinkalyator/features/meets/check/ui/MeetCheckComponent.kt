package me.nemiron.khinkalyator.features.meets.check.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.common_domain.model.MeetId

interface MeetCheckComponent {

    sealed interface Configuration : Parcelable {
        val meetId: MeetId

        @Parcelize
        class Active(override val meetId: MeetId) : Configuration

        @Parcelize
        class Archived(override val meetId: MeetId) : Configuration
    }

    sealed interface Output {
        object MeetEndRequested : Output
    }
}