package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.features.phone.domain.Phone

typealias PersonId = Long

data class Person(
    val id: PersonId,
    val name: String,
    val phone: Phone?,
    val emoji: Emoji
)

@JvmInline
value class Emoji(val value: String)