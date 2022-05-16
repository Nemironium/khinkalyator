package me.nemiron.khinkalyator.core.keyboard

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class CloseKeyboardServiceImpl : CloseKeyboardService {

    private val channel = Channel<Unit>(Channel.UNLIMITED)

    override val closeKeyboardEventsFlow = channel.receiveAsFlow()

    override fun closeKeyboard() {
        channel.trySend(Unit)
    }
}