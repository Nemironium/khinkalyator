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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors

/**
 * Composable for Icon with circle background
 */
@Composable
fun IconWithBackground(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shouldBeColored: Boolean = true,
    contentColor: Color = MaterialTheme.colors.secondary,
    backgroundColor: Color = MaterialTheme.additionalColors.secondaryContainer,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .then(clickableModifier)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            colorFilter = if (shouldBeColored) ColorFilter.tint(contentColor) else null,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
private fun IconsWithBackgroundPreview() {
    KhinkalyatorTheme {
        Column {
            IconWithBackground(painterResource(R.drawable.ic_share_40))
            Spacer(Modifier.height(16.dp))
            IconWithBackground(painterResource(R.drawable.ic_share_40)) {
                // nothing
            }
        }
    }
}