package me.nemiron.khinkalyator.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

fun LocalDateTime.isToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val instantTime = Clock.System.now()
    val todayDate = instantTime.toLocalDateTime(timeZone).date
    return this.date == todayDate
}

fun LocalDateTime.isYesterday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
    val instantTime = Clock.System.now()
    val yesterdayDate = instantTime.minus(1.days).toLocalDateTime(timeZone).date
    return this.date == yesterdayDate
}