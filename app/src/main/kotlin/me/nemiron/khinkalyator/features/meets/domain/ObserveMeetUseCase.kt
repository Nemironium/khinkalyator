package me.nemiron.khinkalyator.features.meets.domain

import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.MeetsStorage

class ObserveMeetUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(meetId: MeetId) = meetsStorage.observeMeet(meetId)
}