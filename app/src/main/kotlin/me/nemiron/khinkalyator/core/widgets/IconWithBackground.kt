package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.utils.contentKhColorFor
import me.nemiron.khinkalyator.core.utils.withElevation

/**
 * Composable for Icon with circle background
 */
@Composable
fun IconWithBackground(
    painter: Painter,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shouldBeColored: Boolean = true,
    elevation: Dp = 0.dp,
    contentColor: Color = contentKhColorFor(backgroundColor),
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = CircleShape, clip = false)
            .zIndex(elevation.value)
            .clip(CircleShape)
            .then(clickableModifier)
            .background(getBackgroundColorForElevation(backgroundColor, elevation)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            colorFilter = if (shouldBeColored) ColorFilter.tint(contentColor) else null,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun getBackgroundColorForElevation(color: Color, elevation: Dp): Color {
    return if (elevation > 0.dp) {
        color.withElevation(elevation)
    } else {
        color
    }
}

@Preview
@Composable
private fun IconsWithBackgroundPreview() {
    KhinkalyatorTheme {
        Column {
            IconWithBackground(
                painter = painterResource(R.drawable.ic_share_40),
                backgroundColor = MaterialTheme.additionalColors.secondaryContainer
            )
            Spacer(Modifier.height(16.dp))
            IconWithBackground(
                painter = painterResource(R.drawable.ic_share_40),
                backgroundColor = MaterialTheme.additionalColors.secondaryContainer,
                onClick = { }
            )
        }
    }
}