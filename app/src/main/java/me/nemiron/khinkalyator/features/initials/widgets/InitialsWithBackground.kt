package me.nemiron.khinkalyator.features.initials.widgets

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
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData

/**
 * Composable for Person initials with circle colored background and border
 */
@Composable
fun SmallInitialsWithBackground(
    data: InitialsViewData,
    modifier: Modifier = Modifier
) {
    InitialsWithBackgroundImpl(
        modifier = modifier,
        data = data,
        initialsTextStyle = MaterialTheme.appTypography.initialsSmall,
        backgroundSize = 32.dp
    )
}

/**
 * Composable for Person initials with circle colored background and border
 */
@Composable
fun BigInitialsWithBackground(
    data: InitialsViewData,
    modifier: Modifier = Modifier
) {
    InitialsWithBackgroundImpl(
        modifier = modifier,
        data = data,
        initialsTextStyle = MaterialTheme.appTypography.initialsBig,
        backgroundSize = 40.dp
    )
}

@Composable
private fun InitialsWithBackgroundImpl(
    data: InitialsViewData,
    initialsTextStyle: TextStyle,
    backgroundSize: Dp,
    modifier: Modifier = Modifier
) {
    val (contentColor, backgroundColor, outlineColor) = data.getColors()
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
            text = data.initials.take(2),
            style = initialsTextStyle,
            color = contentColor
        )
    }
}

@Preview
@Composable
fun InitialsWithBackgroundPreview() {
    KhinkalyatorTheme {
        val data = InitialsViewData("ЖК", Emoji("🐨"))
        Column {
            SmallInitialsWithBackground(data)
            Spacer(Modifier.height(16.dp))
            BigInitialsWithBackground(data)
        }
    }
}