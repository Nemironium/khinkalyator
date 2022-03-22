package me.nemiron.khinkalyator.core.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.ui.theme.additionalColors

/**
 * Composable для создания иконки с круглым фоном
 * @param contentColor цвет для тела иконки. Если не указан,
 * то используется secondaryColor из [MaterialTheme.colors]
 * @param backgroundColor цвет для фона иконки. Если не указан,
 * то используется secondaryContainer из [MaterialTheme.additionalColors]
 * @param backgroundSize размер фона для иконки. Если не указан, то используется 40 [Dp]
 */
@Composable
fun IconWithBackground(
    painter: Painter,
    modifier: Modifier = Modifier,
    backgroundSize: Dp = 40.dp,
    contentDescription: String? = null,
    shouldBeColored: Boolean = true,
    contentColor: Color = MaterialTheme.colors.secondary,
    backgroundColor: Color = MaterialTheme.additionalColors.secondaryContainer
) {
    Box(
        modifier = modifier
            .size(backgroundSize)
            .clip(CircleShape)
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