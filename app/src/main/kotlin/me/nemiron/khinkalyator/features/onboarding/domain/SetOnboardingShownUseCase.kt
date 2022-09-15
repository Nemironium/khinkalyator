package me.nemiron.khinkalyator.features.onboarding.domain

class SetOnboardingShownUseCase(
    private val onBoardingStorage: OnBoardingStorage
) {
    suspend operator fun invoke() = onBoardingStorage.setOnboardingShown()
}