package me.nemiron.khinkalyator.evening.domain

import kotlinx.datetime.Instant

data class Evening(
    val id: Long,
    val date: Instant
)
