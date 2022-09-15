package me.nemiron.khinkalyator.features.onboarding.domain

interface OnBoardingStorage {
    suspend fun isTablesEmpty(): Boolean
    suspend fun initOnboardingData()
    suspend fun clearOnboardingData()
    suspend fun isOnboardingShown(): Boolean
    suspend fun setOnboardingShown()
}