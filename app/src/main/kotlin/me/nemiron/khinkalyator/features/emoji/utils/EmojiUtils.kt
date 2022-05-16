package me.nemiron.khinkalyator.features.emoji.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import me.nemiron.khinkalyator.core.theme.emojiColors
import me.nemiron.khinkalyator.features.emoji.domain.Emoji

@Composable
fun Emoji.getBackgroundColor(): Color = when (this.value) {
    "🐵" -> MaterialTheme.emojiColors.first
    "🦄" -> MaterialTheme.emojiColors.second
    "🐰" -> MaterialTheme.emojiColors.third
    "🐮" -> MaterialTheme.emojiColors.fourth
    "🐱" -> MaterialTheme.emojiColors.fifth
    "🐙" -> MaterialTheme.emojiColors.sixth
    "🐼" -> MaterialTheme.emojiColors.seventh
    "🐨" -> MaterialTheme.emojiColors.eighth
    else -> Color.Transparent
}