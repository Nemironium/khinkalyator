package me.nemiron.khinkalyator.features.onboarding.domain

class InitOnBoardingDataUseCase(
    private val onBoardingStorage: OnBoardingStorage
) {
    suspend operator fun invoke() = onBoardingStorage.initOnboardingData()
}