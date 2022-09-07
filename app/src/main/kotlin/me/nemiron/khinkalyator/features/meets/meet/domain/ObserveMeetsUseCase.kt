package me.nemiron.khinkalyator.features.meets.meet.domain

import kotlinx.coroutines.flow.Flow

class ObserveMeetsUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(): Flow<List<Meet>> {
        return meetsStorage.observeMeets()
    }
}