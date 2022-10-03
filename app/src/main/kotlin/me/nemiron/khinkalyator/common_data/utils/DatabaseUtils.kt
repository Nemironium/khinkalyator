package me.nemiron.khinkalyator.common_data.utils

import android.database.sqlite.SQLiteConstraintException
import me.nemiron.khinkalyator.common_domain.exception.DatabaseConstraintException
import me.nemiron.khinkalyator.common_domain.exception.DatabaseEntityNotExistsException
import timber.log.Timber

internal suspend fun <R> suspendDbCall(block: suspend () -> R): R {
    return try {
        block()
    } catch (e: Exception) {
        wrapApplicationException<R>(e)
    }
}

internal fun <R> dbCall(block: () -> R): R {
    return try {
        block()
    } catch (e: Exception) {
        wrapApplicationException<R>(e)
    }
}

private fun <R> wrapApplicationException(exception: Exception): R {
    val error = when (exception) {
        is SQLiteConstraintException -> DatabaseConstraintException
        is NullPointerException, is NoSuchElementException -> DatabaseEntityNotExistsException
        else -> exception
    }
    Timber.e(error)
    throw error
}