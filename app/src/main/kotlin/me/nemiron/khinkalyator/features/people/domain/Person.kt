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

        val MOCKS = listOf(
            Person(
                id = 1,
                name = "Ритуза",
                phone = null,
                emoji = Emoji("🐵")
            ),
            Person(
                id = 2,
                name = "Элина Зайникеева",
                phone = null,
                emoji = Emoji("🐰")
            ),
            Person(
                id = 3,
                name = "Павел Александров",
                phone = Phone("89041930639"),
                emoji = Emoji("🐙")
            ),
            Person(
                id = 4,
                name = "Жека Кауров",
                phone = null,
                emoji = Emoji("🐨")
            ),
            Person(
                id = 5,
                name = "Томочка Тараненко",
                phone = null,
                emoji = Emoji("🦄")
            ),
            Person(
                id = 6,
                name = "Тёма Шанин",
                phone = null,
                emoji = Emoji("🐼")
            ),
            Person(
                id = 7,
                name = "Макс Цекин",
                phone = null,
                emoji = Emoji("🐮")
            ),
            Person(
                id = 8,
                name = "Настя Станкова",
                phone = null,
                emoji = Emoji("🐱")
            )
        )
    }
}