package me.nemiron.khinkalyator.features.onboarding.domain

import me.nemiron.khinkalyator.features.onboarding.data.PersistentOnBoardingStorage

class IsNeedToShowOnboardingUseCase(
    private val onBoardingStorage: PersistentOnBoardingStorage
) {
    suspend operator fun invoke(): Boolean {
        return onBoardingStorage.isTablesEmpty() && !onBoardingStorage.isOnboardingShown()
    }
}