package me.nemiron.khinkalyator.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import me.nemiron.khinkalyator.core.ui.utils.LocalApplyDarkStatusBarIcons

@Composable
fun KhinkalyatorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val appTypography = AppTypography()
    val appShapes = AppShapes()
    val additionalColors = AdditionalColors()
    val emojiColors = EmojiColors()
    val materialColors = getMaterialColors(darkTheme)

    val rememberLocalDarkIcons = remember { mutableStateMapOf<Int, Unit>() }

    CompositionLocalProvider(
        LocalAdditionalColors provides additionalColors,
        LocalEmojiColors provides emojiColors,
        LocalAppTypography provides appTypography,
        LocalAppShapes provides appShapes,
        LocalApplyDarkStatusBarIcons provides rememberLocalDarkIcons
    ) {
        MaterialTheme(
            colors = materialColors,
            typography = appTypography.toMaterialTypography(),
            shapes = appShapes.toMaterialShapes(),
            content = content
        )
    }
}