package me.nemiron.khinkalyator.core.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography constructor(
    val head1: TextStyle,
    val head2: TextStyle,
    val head3: TextStyle,
    val medium: TextStyle,
    val text1: TextStyle,
    val text2: TextStyle,
    val button: TextStyle,
    val emojiBig: TextStyle,
    val emojiSmall: TextStyle,
    val initialsBig: TextStyle,
    val initialsSmall: TextStyle
) {
    constructor(
        defaultFontFamily: FontFamily = FontFamily.Default,
        head1: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        head2: TextStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 26.sp
        ),
        head3: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 21.sp
        ),
        medium: TextStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 18.8.sp
        ),
        text1: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 18.8.sp
        ),
        text2: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 16.4.sp
        ),
        button: TextStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 23.7.sp
        ),
        emojiBig: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 42.sp
        ),
        emojiSmall: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 33.sp
        ),
        initialsBig: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 18.sp
        ),
        initialsSmall: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    ) : this(
        head1 = head1.withDefaultFontFamily(defaultFontFamily),
        head2 = head2.withDefaultFontFamily(defaultFontFamily),
        head3 = head3.withDefaultFontFamily(defaultFontFamily),
        medium = medium.withDefaultFontFamily(defaultFontFamily),
        text1 = text1.withDefaultFontFamily(defaultFontFamily),
        text2 = text2.withDefaultFontFamily(defaultFontFamily),
        button = button.withDefaultFontFamily(defaultFontFamily),
        emojiBig = emojiBig.withDefaultFontFamily(defaultFontFamily),
        emojiSmall= emojiSmall.withDefaultFontFamily(defaultFontFamily),
        initialsBig= initialsBig.withDefaultFontFamily(defaultFontFamily),
        initialsSmall= initialsSmall.withDefaultFontFamily(defaultFontFamily)
    )
}

// TODO: add shapes for required material2 components (dialogs and etc)
fun AppTypography.toMaterialTypography(): Typography {
    return Typography(
        h6 = head1 // toolbar title
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(defaultFontFamily = FontFamily.Default)
}

val MaterialTheme.appTypography: AppTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTypography.current

private fun TextStyle.withDefaultFontFamily(default: FontFamily): TextStyle {
    return if (fontFamily != null) this else copy(fontFamily = default)
}