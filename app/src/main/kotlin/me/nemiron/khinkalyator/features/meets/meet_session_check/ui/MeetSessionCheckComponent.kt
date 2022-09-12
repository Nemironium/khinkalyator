package me.nemiron.khinkalyator.features.meets.meet_session_check.ui

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import me.nemiron.khinkalyator.features.meets.domain.MeetId

interface MeetSessionCheckComponent {

    sealed interface Configuration : Parcelable {
        val meetId: MeetId

        @Parcelize
        class ActiveSession(override val meetId: MeetId) : Configuration

        @Parcelize
        class ArchivedSession(override val meetId: MeetId) : Configuration
    }

    sealed interface Output {
        object SessionEndRequested : Output
    }
}