package me.nemiron.khinkalyator.features.onboarding.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.SuspendSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.features.onboarding.domain.OnBoardingStorage

@OptIn(ExperimentalSettingsApi::class)
class PersistentOnBoardingStorage(
    db: KhinkalytorDatabase,
    private val suspendSettings: SuspendSettings,
    private val coroutineDispatcher: CoroutineDispatcher
) : OnBoardingStorage {

    private val predefinedDataQueries = db.predefinedDataQueries
    private val peopleQueries = db.personQueries
    private val restaurantsQueries = db.restaurantQueries
    private val dishesQueries = db.dishQueries

    override suspend fun isTablesEmpty(): Boolean {
        return withContext(coroutineDispatcher) {
            predefinedDataQueries.transactionWithResult {
                val isPeopleTableEmpty = peopleQueries.getRowsCount().executeAsOne() == 0L
                val isRestaurantsTableEmpty = restaurantsQueries.getRowsCount().executeAsOne() == 0L
                val isDishesTableEmpty = dishesQueries.getRowsCount().executeAsOne() == 0L
                isPeopleTableEmpty && isRestaurantsTableEmpty && isDishesTableEmpty
            }
        }
    }

    override suspend fun initOnboardingData() {
        withContext(coroutineDispatcher) {
            predefinedDataQueries.transaction {
                predefinedDataQueries.insertDefaultPeople()
                predefinedDataQueries.insertDefaultRestaurants()
                predefinedDataQueries.insertDefaultDishes()
                predefinedDataQueries.insertDefaultMeets()
                predefinedDataQueries.insertDefaultMeetsPeople()
                predefinedDataQueries.insertDefaultOrders()
                predefinedDataQueries.insertDefaultPeopleOrders()
            }
        }
    }

    override suspend fun clearOnboardingData() {
        withContext(coroutineDispatcher) {
            predefinedDataQueries.transaction {
                predefinedDataQueries.deleteDefaultMeets()
                predefinedDataQueries.deleteDefaultPeople()
                predefinedDataQueries.deleteDefaultRestaurant()
            }
        }
    }

    override suspend fun isOnboardingShown(): Boolean {
        return suspendSettings.getBoolean(OnboardingFlagKey, false)
    }

    override suspend fun setOnboardingShown() {
        suspendSettings.putBoolean(OnboardingFlagKey, true)
    }

    private companion object {
        const val OnboardingFlagKey = "Onboarding_flag_key"
    }
}