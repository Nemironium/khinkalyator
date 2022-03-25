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
        val supportedEmojis = arrayOf("🐵", "🦄", "🐰", "🐮", "🐱", "🐙", "🐼", "🐨")
        // TODO: add additional background and content colors for other emojis
        val animalEmojis = arrayOf(
            /* subgroup: animal-mammal */
            "🐵",
            "🐒",
            "🦍",
            "🦧",
            "🐶",
            "🐕",
            "🦮",
            "🐕‍🦺",
            "🐩",
            "🐺",
            "🦊",
            "🦝",
            "🐱",
            "🐈",
            "🐈‍⬛",
            "🦁",
            "🐯",
            "🐅",
            "🐆",
            "🐴",
            "🐎",
            "🦄",
            "🦓",
            "🦌",
            "🦬",
            "🐮",
            "🐂",
            "🐃",
            "🐄",
            "🐷",
            "🐖",
            "🐗",
            "🐽",
            "🐏",
            "🐑",
            "🐐",
            "🐪",
            "🐫",
            "🦙",
            "🦒",
            "🐘",
            "🦣",
            "🦏",
            "🦛",
            "🐭",
            "🐁",
            "🐀",
            "🐹",
            "🐰",
            "🐇",
            "🐿️",
            "🦫",
            "🦔",
            "🦇",
            "🐻",
            "🐻‍❄️",
            "🐨",
            "🐼",
            "🦥",
            "🦦",
            "🦨",
            "🦘",
            "🦡",

            /* subgroup: animal-bird */
            "🦃",
            "🐔",
            "🐓",
            "🐣",
            "🐤",
            "🐥",
            "🐦",
            "🐧",
            "🕊️",
            "🦅",
            "🦆",
            "🦢",
            "🦉",
            "🦤",
            "🪶",
            "🦩",
            "🦚",
            "🦜",

            /* subgroup: animal-amphibian */
            "🐸",

            /* subgroup: animal-reptile */
            "🐊",
            "🐢",
            "🦎",
            "🐍",
            "🐲",
            "🐉",
            "🦕",
            "🦖",

            /* subgroup: animal-marine */
            "🐳",
            "🐋",
            "🐬",
            "🦭",
            "🐟",
            "🐠",
            "🐡",
            "🦈",
            "🐙"
        )

        const val RANDOM_FROM = 0
        val RANDOM_UNTIL = supportedEmojis.size - 1
    }
}