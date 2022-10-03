package me.nemiron.khinkalyator.common_domain.exception

sealed class ApplicationException(cause: Throwable? = null) : Exception(cause)

object DatabaseConstraintException : ApplicationException()

object DatabaseEntityNotExistsException : ApplicationException()

object PersonInActiveMeetException : ApplicationException()

object RestaurantInActiveMeetException : ApplicationException()
