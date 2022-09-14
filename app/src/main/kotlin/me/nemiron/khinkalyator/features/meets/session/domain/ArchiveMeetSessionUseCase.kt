package me.nemiron.khinkalyator.features.meets.session.domain

import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.MeetsStorage

class ArchiveMeetSessionUseCase(
    private val meetsStorage: MeetsStorage
) {
    // TODO: add custom app-specific exception
    suspend operator fun invoke(meetId: MeetId) {
        val meetSession = meetsStorage.getMeetSession(meetId) ?: throw NoSuchElementException()

    }
}