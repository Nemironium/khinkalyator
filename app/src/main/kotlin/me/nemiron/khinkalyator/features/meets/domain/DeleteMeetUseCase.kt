package me.nemiron.khinkalyator.features.meets.domain

import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.MeetsStorage

class DeleteMeetUseCase(
    private val meetsStorage: MeetsStorage

) {
    suspend operator fun invoke(meetId: MeetId) = meetsStorage.deleteMeet(meetId)
}