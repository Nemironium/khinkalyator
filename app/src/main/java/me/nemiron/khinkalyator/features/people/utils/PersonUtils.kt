package me.nemiron.khinkalyator.features.people.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import me.nemiron.khinkalyator.core.ui.theme.initialsColors
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person

/*
* first – content color
* second – background color
* third – outline color
* */
// TODO: add all 8 colors when design will be ready
@Composable
fun Person.getColorsForInitials(): Triple<Color, Color, Color> {
    return emoji.getColorsForInitials()
}

@Composable
fun Emoji.getColorsForInitials(): Triple<Color, Color, Color> {
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