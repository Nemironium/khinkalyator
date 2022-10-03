package me.nemiron.khinkalyator.features.meets.domain

import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.model.Meet

class ArchiveMeetUseCase(
    private val meetsStorage: MeetsStorage
) {

    suspend operator fun invoke(meetId: MeetId, tips: Meet.Tips?) =
        meetsStorage.archiveMeet(meetId, tips)
}