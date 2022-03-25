package me.nemiron.khinkalyator.features.meets.page.ui

import me.nemiron.khinkalyator.features.meets.domain.MeetId

interface MeetsPageComponent {

    val meetsViewData: List<MeetFullViewData>

    fun onMeetAddClick()

    fun onMeetClick(meetId: MeetId)

    sealed interface Output {
        object NewMeetRequested : Output
        class MeetRequested(val meetId: MeetId) : Output
    }
}