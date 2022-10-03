package me.nemiron.khinkalyator.core.exception.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

// TODO: add needed exception handling
fun <T> Flow<T>.handleErrors(): Flow<T> =
    catch { e ->
        when (e) {
        }
    }