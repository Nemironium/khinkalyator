package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.coroutines.flow.Flow

class ObserveMeetSessionUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(meetId: MeetId): Flow<MeetSession?> {
        return meetsStorage.observeMeetSession(meetId)
    }
}