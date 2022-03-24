package me.nemiron.khinkalyator.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.theme.appTypography
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.people.utils.getColorsForInitials

/**
 * Composable for Person initials with circle colored background and border
 */
@Composable
fun SmallInitialsWithBackground(
    initials: String,
    emoji: Emoji,
    modifier: Modifier = Modifier
) {
    InitialsWithBackgroundImpl(
        modifier = modifier,
        initials = initials,
        initialsTextStyle = MaterialTheme.appTypography.initialsSmall,
        emoji = emoji,
        backgroundSize = 32.dp
    )
}

/**
 * Composable for Person initials with circle colored background and border
 */
@Composable
fun BigInitialsWithBackground(
    initials: String,
    emoji: Emoji,
    modifier: Modifier = Modifier
) {
    InitialsWithBackgroundImpl(
        modifier = modifier,
        initials = initials,
        initialsTextStyle = MaterialTheme.appTypography.initialsBig,
        emoji = emoji,
        backgroundSize = 40.dp
    )
}

@Composable
private fun InitialsWithBackgroundImpl(
    initials: String,
    initialsTextStyle: TextStyle,
    emoji: Emoji,
    backgroundSize: Dp,
    modifier: Modifier = Modifier
) {
    val (contentColor, backgroundColor, outlineColor) = emoji.getColorsForInitials()
    Box(
        modifier = modifier
            .size(backgroundSize)
            .border(
                width = 1.dp,
                color = outlineColor,
                shape = CircleShape
            )
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.substring(0..1).uppercase(),
            style = initialsTextStyle,
            color = contentColor
        )
    }
}

@Preview
@Composable
fun InitialsWithBackgroundPreview() {
    KhinkalyatorTheme {
        val emoji = Emoji("🐨")
        Column {
            SmallInitialsWithBackground(
                initials = "жк",
                emoji = emoji

            )
            Spacer(Modifier.height(16.dp))
            BigInitialsWithBackground(
                initials = "жк",
                emoji = emoji
            )
        }
    }
}