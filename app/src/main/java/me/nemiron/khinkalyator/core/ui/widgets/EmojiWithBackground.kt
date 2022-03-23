package me.nemiron.khinkalyator.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.theme.appTypography
import me.nemiron.khinkalyator.core.ui.theme.emojiColors

// FIXME: algorithm to match emoji and background color

/**
 * Composable for Emoji with circle colored background
 */
@Composable
fun SmallEmojiWithBackground(
    emoji: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.emojiColors.first
) {
    EmojiWithBackgroundImpl(
        modifier = modifier,
        emoji = emoji,
        backgroundColor = backgroundColor,
        backgroundSize = 48.dp,
        emojiTextStyle = MaterialTheme.appTypography.emojiSmall,
        isOutlined = false
    )
}

/**
 * Composable for Emoji with circle colored background
 * @param clickableModifier should only be used with [Modifier.clickable]
 */
@Composable
fun BigEmojiWithBackground(
    emoji: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    backgroundColor: Color = MaterialTheme.emojiColors.first,
    clickableModifier: Modifier = Modifier
) {
    EmojiWithBackgroundImpl(
        modifier = modifier,
        emoji = emoji,
        emojiTextStyle = MaterialTheme.appTypography.emojiBig,
        backgroundSize = 76.dp,
        backgroundColor = backgroundColor,
        isOutlined = isSelected,
        outlinePadding = PaddingValues(6.dp),
        clickableModifier = clickableModifier
    )
}

@Composable
private fun EmojiWithBackgroundImpl(
    emoji: String,
    emojiTextStyle: TextStyle,
    backgroundSize: Dp,
    backgroundColor: Color,
    isOutlined: Boolean,
    modifier: Modifier = Modifier,
    outlineColor: Color = MaterialTheme.colors.secondary,
    outlinePadding: PaddingValues = PaddingValues(0.dp),
    clickableModifier: Modifier = Modifier
) {
    val outlineModifier = if (isOutlined) {
        Modifier
            .border(
                width = 2.dp,
                color = outlineColor,
                shape = CircleShape
            )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(backgroundSize)
            .clip(CircleShape)
            .then(clickableModifier)
            .then(outlineModifier)
            .padding(outlinePadding)
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, style = emojiTextStyle)
    }
}

@Preview
@Composable
fun EmojiWithBackgroundPreview() {
    KhinkalyatorTheme {
        Column {
            SmallEmojiWithBackground("🐨")
            Spacer(Modifier.height(16.dp))
            BigEmojiWithBackground("🐨")
            Spacer(Modifier.height(16.dp))
            BigEmojiWithBackground("🐨", isSelected = true)
            Spacer(Modifier.height(16.dp))
            BigEmojiWithBackground(
                "🐨",
                clickableModifier = Modifier.clickable { }
            )
            Spacer(Modifier.height(16.dp))
            BigEmojiWithBackground(
                "🐨",
                isSelected = true,
                clickableModifier = Modifier.clickable { })
        }
    }
}