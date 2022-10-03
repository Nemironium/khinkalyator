package me.nemiron.khinkalyator.common_data.mapper

import me.nemiron.khinkalyator.common_domain.model.Phone
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.common_domain.model.Person

internal fun mapToPerson(
    id: Long,
    name: String,
    emoji: String,
    phone: String?,
    type: Int
): Person {
    val status = when (type) {
        0 -> Person.Status.Archived
        1 -> Person.Status.Active
        else -> throw IllegalArgumentException("Not supported Person type")
    }
    return Person(id, name, phone?.let(::Phone), Emoji(emoji), status)
}