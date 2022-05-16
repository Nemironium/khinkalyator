package me.nemiron.khinkalyator.features.people.domain

import me.nemiron.khinkalyator.features.emoji.domain.EmojiProvider
import me.nemiron.khinkalyator.features.phone.domain.Phone

class AddPersonUseCase(
    private val peopleStorage: PeopleStorage,
    private val emojiProvider: EmojiProvider
) {

    suspend operator fun invoke(name: String, phone: String?) {
        val emoji = emojiProvider.getNextEmoji()
        peopleStorage.addPerson(
            name = name,
            phone = phone?.let { Phone(it) },
            emoji = emoji
        )
    }
}