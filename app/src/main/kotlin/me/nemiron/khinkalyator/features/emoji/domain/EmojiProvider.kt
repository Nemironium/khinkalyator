package me.nemiron.khinkalyator.features.emoji.domain

import kotlin.random.Random

/**
 * Stores only fully-qualified animal emojis from https://unicode.org/emoji/
 */
class EmojiProvider {

    fun getNextEmoji(): Emoji {
        val randomNumber = Random.nextInt(from = RANDOM_FROM, until = RANDOM_UNTIL)
        val randomEmoji = supportedEmojis[randomNumber]
        return Emoji(randomEmoji)
    }

    private companion object {
        val supportedEmojis = arrayOf("đĩ", "đĻ", "đ°", "đŽ", "đą", "đ", "đŧ", "đ¨")
        // TODO: add additional background and content colors for other emojis
        val animalEmojis = arrayOf(
            /* subgroup: animal-mammal */
            "đĩ",
            "đ",
            "đĻ",
            "đĻ§",
            "đļ",
            "đ",
            "đĻŽ",
            "đâđĻē",
            "đŠ",
            "đē",
            "đĻ",
            "đĻ",
            "đą",
            "đ",
            "đââŦ",
            "đĻ",
            "đ¯",
            "đ",
            "đ",
            "đ´",
            "đ",
            "đĻ",
            "đĻ",
            "đĻ",
            "đĻŦ",
            "đŽ",
            "đ",
            "đ",
            "đ",
            "đˇ",
            "đ",
            "đ",
            "đŊ",
            "đ",
            "đ",
            "đ",
            "đĒ",
            "đĢ",
            "đĻ",
            "đĻ",
            "đ",
            "đĻŖ",
            "đĻ",
            "đĻ",
            "đ­",
            "đ",
            "đ",
            "đš",
            "đ°",
            "đ",
            "đŋī¸",
            "đĻĢ",
            "đĻ",
            "đĻ",
            "đģ",
            "đģââī¸",
            "đ¨",
            "đŧ",
            "đĻĨ",
            "đĻĻ",
            "đĻ¨",
            "đĻ",
            "đĻĄ",

            /* subgroup: animal-bird */
            "đĻ",
            "đ",
            "đ",
            "đŖ",
            "đ¤",
            "đĨ",
            "đĻ",
            "đ§",
            "đī¸",
            "đĻ",
            "đĻ",
            "đĻĸ",
            "đĻ",
            "đĻ¤",
            "đĒļ",
            "đĻŠ",
            "đĻ",
            "đĻ",

            /* subgroup: animal-amphibian */
            "đ¸",

            /* subgroup: animal-reptile */
            "đ",
            "đĸ",
            "đĻ",
            "đ",
            "đ˛",
            "đ",
            "đĻ",
            "đĻ",

            /* subgroup: animal-marine */
            "đŗ",
            "đ",
            "đŦ",
            "đĻ­",
            "đ",
            "đ ",
            "đĄ",
            "đĻ",
            "đ"
        )

        const val RANDOM_FROM = 0
        val RANDOM_UNTIL = supportedEmojis.size - 1
    }
}