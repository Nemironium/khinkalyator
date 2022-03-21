package me.nemiron.khinkalyator.core.ui.utils

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
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding

fun Modifier.statusBar(
    color: Color,
    darkIcons: Boolean = color.luminance() > 0.5
): Modifier = composed {
    if (darkIcons) {
        DarkStatusBarIcons()
    }

    val statusBarHeightPx = LocalWindowInsets.current.statusBars.top.toFloat()
    this
        .drawBehind { drawRect(color, Offset.Zero, Size(size.width, statusBarHeightPx)) }
        .statusBarsPadding()
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