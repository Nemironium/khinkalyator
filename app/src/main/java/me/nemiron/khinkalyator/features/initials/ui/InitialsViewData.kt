package me.nemiron.khinkalyator.features.initials.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import me.nemiron.khinkalyator.core.ui.theme.initialsColors
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person

data class InitialsViewData(
    val initials: String,
    private val emoji: Emoji
) {
    /**
     * first – content color
     * second – background color
     * third – outline color
     */
    @Composable
    fun getColors(): Triple<Color, Color, Color> {
        return emoji.getColorsForInitials()
    }
}

fun Person.toInitialsViewData(): InitialsViewData {
    val firstCapitalizedChar = name.firstOrNull()?.uppercase().orEmpty()
    val secondCapitalizedChar = name.substringAfter(" ").firstOrNull()?.uppercase().orEmpty()
    return InitialsViewData(
        initials = firstCapitalizedChar + secondCapitalizedChar,
        emoji = emoji
    )
}

@Composable
private fun Emoji.getColorsForInitials(): Triple<Color, Color, Color> {
    val contentColor = when (this.value) {
        "🐵" -> MaterialTheme.initialsColors.first
        "🦄" -> MaterialTheme.initialsColors.second
        "🐰" -> MaterialTheme.initialsColors.third
        "🐮" -> MaterialTheme.initialsColors.fourth
        "🐱" -> MaterialTheme.initialsColors.first
        "🐙" -> MaterialTheme.initialsColors.second
        "🐼" -> MaterialTheme.initialsColors.third
        "🐨" -> MaterialTheme.initialsColors.fourth
        else -> Color.Transparent
    }
    val backgroundColor = contentColor.copy(alpha = 0.2f)
    val outlineColor = contentColor.copy(alpha = 0.6f)
    return Triple(contentColor, backgroundColor, outlineColor)
}