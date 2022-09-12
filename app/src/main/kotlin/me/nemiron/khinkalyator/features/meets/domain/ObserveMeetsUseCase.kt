package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveMeetsUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(): Flow<List<Meet>> {
        val listComparator = compareByDescending<Meet> { meet ->
            meet.type is Meet.Type.Active
        }.thenByDescending { meet ->
            meet.type.createTime
        }

        return meetsStorage
            .observeMeets()
            .map { meets ->
                meets.sortedWith(listComparator)
            }
    }
}