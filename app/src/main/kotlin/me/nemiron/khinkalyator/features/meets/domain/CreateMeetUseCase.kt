package me.nemiron.khinkalyator.features.meets.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantsStorage

class CreateMeetUseCase(
    private val peopleStorage: PeopleStorage,
    private val restaurantsStorage: RestaurantsStorage,
    private val meetsStorage: MeetsStorage
) {
    suspend operator fun invoke(restaurantId: RestaurantId, personIds: List<PersonId>): MeetId {
        val restaurant = restaurantsStorage.getRestaurant(restaurantId)
        val people = personIds.mapNotNull { peopleStorage.getPerson(it) }

        // TODO: add custom app-specific exception
        return when {
            restaurant == null -> {
                throw NoSuchElementException()
            }
            people.size != personIds.size -> {
                throw NoSuchElementException()
            }
            else -> {
                val currentTimeZone = TimeZone.currentSystemDefault()
                val todayDate = Clock.System.now().toLocalDateTime(currentTimeZone)
                meetsStorage.createMeetSession(restaurant, people, todayDate)
            }
        }
    }
}