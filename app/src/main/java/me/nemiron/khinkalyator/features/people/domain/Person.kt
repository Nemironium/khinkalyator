package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.phone.domain.Phone

typealias PersonId = Long

data class Person(
    val id: PersonId,
    val name: String,
    val phone: Phone?,
    val emoji: Emoji,
    val status: Status = Status.Active
) {
    enum class Status(val code: Int) {
        Deleted(-1),
        Active(0)
    }

    // TODO: support Person deleting and replacing it with DELETED model
    companion object {
        private val DELETED = Person(
            id = -1,
            name = "",
            phone = null,
            emoji = Emoji("☠️"),
            status = Status.Deleted
        )
    }
}