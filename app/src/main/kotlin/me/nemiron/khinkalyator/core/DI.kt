package me.nemiron.khinkalyator.core

import me.nemiron.khinkalyator.core.keyboard.CloseKeyboardService
import me.nemiron.khinkalyator.core.keyboard.CloseKeyboardServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf

import org.koin.dsl.module

val coreModule = module {
    singleOf(::CloseKeyboardServiceImpl) { bind<CloseKeyboardService>() }
}