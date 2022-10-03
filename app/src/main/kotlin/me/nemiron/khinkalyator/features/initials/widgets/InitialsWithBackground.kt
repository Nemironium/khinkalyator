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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData

/**
 * Composable for Person initials with circle colored background and border
 */
@Composable
fun SmallInitialsWithBackground(
    data: InitialsViewData,
    modifier: Modifier = Modifier
) {
    InitialsWithBackground(
        modifier = modifier,
        data = data,
        textStyle = MaterialTheme.appTypography.initialsSmall,
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
    InitialsWithBackground(
        modifier = modifier,
        data = data,
        textStyle = MaterialTheme.appTypography.initialsBig,
        backgroundSize = 40.dp
    )
}

/**
 * Composable for Person initials with circle colored background and border
 * @param overlayColor – optional [Color] for additional background color overlaid
 * above main backgroundColor from [InitialsViewData.getColors]
 */
@Composable
fun InitialsWithBackground(
    data: InitialsViewData,
    textStyle: TextStyle,
    backgroundSize: Dp,
    modifier: Modifier = Modifier,
    overlayColor: Color? = null
) {
    val (contentColor, backgroundColor, outlineColor) = data.getColors()
    val overlayColorModifier = if (overlayColor != null) {
        Modifier.background(
            color = overlayColor,
            shape = CircleShape
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .size(backgroundSize)
            .then(overlayColorModifier)
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
            style = textStyle,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun InitialsWithBackgroundPreview() {
    KhinkalyatorTheme {
        val data = InitialsViewData("ЖК", Emoji("🐨"))
        Column {
            SmallInitialsWithBackground(data)
            Spacer(Modifier.height(16.dp))
            BigInitialsWithBackground(data)
        }
    }
}