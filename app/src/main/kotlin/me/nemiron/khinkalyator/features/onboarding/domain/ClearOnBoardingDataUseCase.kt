package me.nemiron.khinkalyator.features.onboarding.domain

class ClearOnBoardingDataUseCase(
    private val onBoardingStorage: OnBoardingStorage
) {
    suspend operator fun invoke() = onBoardingStorage.clearOnboardingData()
}