package me.nemiron.khinkalyator.features.meets.session.domain

import kotlinx.coroutines.flow.Flow
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.meets.domain.MeetsStorage

class ObserveMeetSessionUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(meetId: MeetId): Flow<MeetSession?> {
        // TODO: (when DB ready) combine data from all storages instead observe only from MeetsStorage
        return meetsStorage.observeMeetSession(meetId)
    }
}