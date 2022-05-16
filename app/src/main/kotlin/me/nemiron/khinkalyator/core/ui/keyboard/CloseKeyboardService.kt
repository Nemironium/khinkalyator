package me.nemiron.khinkalyator.core.ui.keyboard

import kotlinx.coroutines.flow.Flow

interface CloseKeyboardService {

    val closeKeyboardEventsFlow: Flow<Unit>

    fun closeKeyboard()
}