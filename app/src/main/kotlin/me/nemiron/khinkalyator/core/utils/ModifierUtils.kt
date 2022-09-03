package me.nemiron.khinkalyator.core.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

/**
 * Dismiss when tapping outside elements
 *
 * If in downstream elements hierarchy OnTap events are consumed, then nothing will happen
 *
 * See [Modifier.consumeOnTapGestures]
 */
fun Modifier.dismissOnTapOutsideElements(): Modifier = composed {
    val currentContext = LocalContext.current

    pointerInput(Unit) {
        detectTapGestures(onTap = {
            dispatchOnBackPressed(currentContext)
        })
    }
}

/**
 * Consume OnTap events to prevent it transmitting to upstream elements hierarchy
 */
fun Modifier.consumeOnTapGestures(): Modifier = composed {
    pointerInput(Unit) {
        detectTapGestures(onTap = {
            // nothing
        })
    }
}

fun Modifier.statusBar(
    color: Color,
    darkIcons: Boolean = color.luminance() > 0.5
): Modifier = composed {
    if (darkIcons) {
        DarkStatusBarIcons()
    }

    val currentDensity = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(currentDensity).toFloat()

    drawBehind {
        drawRect(color, Offset.Zero, Size(size.width, statusBarHeightPx))
    }.statusBarsPadding()
}

val LocalApplyDarkStatusBarIcons = staticCompositionLocalOf { mutableStateMapOf<Int, Unit>() }

@Composable
fun DarkStatusBarIcons() {
    val key = currentCompositeKeyHash
    val applyDarkStatusBarIcons = LocalApplyDarkStatusBarIcons.current

    DisposableEffect(Unit) {
        applyDarkStatusBarIcons[key] = Unit
        onDispose { applyDarkStatusBarIcons.remove(key) }
    }
}