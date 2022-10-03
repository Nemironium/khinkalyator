package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.RestaurantId
import me.nemiron.khinkalyator.common_domain.model.MeetId

class CreateMeetUseCase(
    private val meetsStorage: MeetsStorage
) {
    suspend operator fun invoke(restaurantId: RestaurantId, personIds: List<PersonId>): MeetId {
        val currentTimeZone = TimeZone.currentSystemDefault()
        val todayDate = Clock.System.now().toLocalDateTime(currentTimeZone)

        return meetsStorage.createMeet(restaurantId, personIds, Meet.Status.Active(todayDate))
    }
}