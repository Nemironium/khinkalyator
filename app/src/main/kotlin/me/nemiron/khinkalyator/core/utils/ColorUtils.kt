package me.nemiron.khinkalyator.core.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Colors
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.unit.Dp
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.AdditionalColors
import kotlin.math.ln

/**
 * @return the matching content color for backgroundColor.
 * If backgroundColor is not present in the theme's [Colors] and [AdditionalColors],
 * then returns [Color.Unspecified].
 */
@Composable
@ReadOnlyComposable
fun contentKhColorFor(backgroundColor: Color): Color {
    val materialColors = MaterialTheme.colors
    val additionalColors = MaterialTheme.additionalColors

    val color = materialColors.contentColorFor(backgroundColor)

    return if (color.isUnspecified) {
        when (backgroundColor) {
            additionalColors.onSurfaceContainer -> materialColors.onSurface
            additionalColors.secondaryBackground -> materialColors.onSecondary
            additionalColors.secondaryContainer -> materialColors.secondary
            else -> Color.Unspecified
        }
    } else {
        color
    }
}

/**
 * Applies a [Color.White] overlay to this color based on the [elevation]. This increases visibility
 * of elevation for surfaces in a dark theme.
 *
 * TODO: Remove when public https://issuetracker.google.com/155181601
 */
fun Color.withElevation(elevation: Dp): Color {
    val foreground = calculateForeground(elevation)
    return foreground.compositeOver(this)
}

/**
 * @return the alpha-modified [Color.White] to overlay on top of the surface color to produce
 * the resultant color.
 */
private fun calculateForeground(elevation: Dp): Color {
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return Color.White.copy(alpha = alpha)
}