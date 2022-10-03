package me.nemiron.khinkalyator.features.onboarding

import me.nemiron.khinkalyator.features.onboarding.data.PersistentOnBoardingStorage
import me.nemiron.khinkalyator.features.onboarding.domain.ClearOnBoardingDataUseCase
import me.nemiron.khinkalyator.features.onboarding.domain.InitOnBoardingDataUseCase
import me.nemiron.khinkalyator.features.onboarding.domain.IsNeedToShowOnboardingUseCase
import me.nemiron.khinkalyator.features.onboarding.domain.OnBoardingStorage
import me.nemiron.khinkalyator.features.onboarding.domain.SetOnboardingShownUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val onBoardingModule = module {
    singleOf(::PersistentOnBoardingStorage) { bind<OnBoardingStorage>() }
    factoryOf(::InitOnBoardingDataUseCase)
    factoryOf(::ClearOnBoardingDataUseCase)
    factoryOf(::IsNeedToShowOnboardingUseCase)
    factoryOf(::SetOnboardingShownUseCase)
}