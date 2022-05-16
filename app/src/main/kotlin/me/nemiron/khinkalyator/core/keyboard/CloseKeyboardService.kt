package me.nemiron.khinkalyator.core.keyboard

import kotlinx.coroutines.flow.Flow

interface CloseKeyboardService {

    val closeKeyboardEventsFlow: Flow<Unit>

    fun closeKeyboard()
}