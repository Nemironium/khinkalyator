package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.model.Meet

class ObserveMeetsUseCase(
    private val meetsStorage: MeetsStorage
) {
    operator fun invoke(): Flow<List<Meet>> {
        val listComparator = compareByDescending<Meet> { meet ->
            meet.status is Meet.Status.Active
        }.thenByDescending { meet ->
            meet.status.createTime
        }

        return meetsStorage
            .observeMeets()
            .map { meets ->
                meets.sortedWith(listComparator)
            }
    }
}