package me.nemiron.khinkalyator.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// colors from Figma project
private object RawColors {
    val blackMain: Color = Color(0xFF353535)
    val blackText: Color = Color(0xFF000000)
    val blackAlpha50: Color = Color(0x80000000)
    val purpleButton: Color = Color(0xFF8779D3)
    val whiteMain: Color = Color(0xFFFFFFFF)
    val whiteBack: Color = Color(0xFFFBFAFD)
    val yellowTop: Color = Color(0xFFFEDE60)
    val silver: Color = Color(0xFFC7C7C7)
    val tapa: Color = Color(0xFF7A776C)
    val selago: Color = Color(0xFFF2EBFC)

    val blueStroke: Color = Color(0xFFD5E6F7)
    val blueLetter: Color = Color(0xFF336093)
    val blueLight: Color = Color(0xFFEAF1F8)
    val blueLight2: Color = Color(0xFFE6EAFF)
    val greenLight: Color = Color(0xFFE8F6EB)
    val greenLetter: Color = Color(0xFF4F9F53)
    val greenBorder: Color = Color(0xFFB8F0C1)
    val purpleLetter: Color = Color(0xFF7C4ABA)
    val purpleBack: Color = Color(0xFFEEE9FF)
    val purpleStroke: Color = Color(0xFFE5DAF1)
    val grayVeryLight: Color = Color(0xFFF5F5F5)
}

@Immutable
data class AdditionalColors(
    val secondaryOnSurface: Color = RawColors.silver,
    val secondaryOnBackground: Color = RawColors.tapa,
    val secondaryOnPrimary: Color = RawColors.blackAlpha50
)

val LocalAdditionalColors = staticCompositionLocalOf {
    AdditionalColors()
}

// TODO: think about dark theme
fun getMaterialColors(darkTheme: Boolean): Colors {
    return lightColors(
        primary = RawColors.yellowTop,
        secondary = RawColors.purpleButton,
        secondaryVariant = RawColors.selago,
        background = RawColors.whiteBack,
        surface = RawColors.whiteMain,
        onPrimary = RawColors.blackMain,
        onSecondary = RawColors.whiteMain,
        onBackground = RawColors.blackMain,
        onSurface = RawColors.blackText
    )
}