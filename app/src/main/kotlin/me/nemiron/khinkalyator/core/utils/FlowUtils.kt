package me.nemiron.khinkalyator.core.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> StateFlow<T>.toComposeState(coroutineScope: CoroutineScope): State<T> {
    val state: MutableState<T> = mutableStateOf(this.value)
    coroutineScope.launch {
        this@toComposeState.collect {
            state.value = it
        }
    }
    return state
}

fun <T> Flow<T>.toComposeState(initialValue: T, coroutineScope: CoroutineScope): State<T> {
    val state: MutableState<T> = mutableStateOf(initialValue)
    coroutineScope.launch {
        this@toComposeState.collect {
            state.value = it
        }
    }
    return state
}