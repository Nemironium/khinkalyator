package me.nemiron.khinkalyator.features.meets.home_page.ui

import me.nemiron.khinkalyator.common_domain.model.MeetId

interface MeetsPageComponent {

    val meetsViewData: List<MeetHomePageViewData>

    fun onMeetAddClick()

    fun onMeetClick(meetId: MeetId)

    sealed interface Output {
        object NewMeetRequested : Output
        class MeetRequested(val meetId: MeetId) : Output
    }
}