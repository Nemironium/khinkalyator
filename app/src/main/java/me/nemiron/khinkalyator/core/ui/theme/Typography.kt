package me.nemiron.khinkalyator.core.ui.theme

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
    val button: TextStyle
) {
    constructor(
        defaultFontFamily: FontFamily = FontFamily.Default,
        head1: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        head2: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 25.78.sp
        ),
        head3: TextStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 21.09.sp
        ),
        medium: TextStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 18.75.sp
        ),
        text1: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 18.75.sp
        ),
        text2: TextStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 16.41.sp
        ),
        button: TextStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 23.74.sp
        )
    ) : this(
        head1 = head1.withDefaultFontFamily(defaultFontFamily),
        head2 = head2.withDefaultFontFamily(defaultFontFamily),
        head3 = head3.withDefaultFontFamily(defaultFontFamily),
        medium = medium.withDefaultFontFamily(defaultFontFamily),
        text1 = text1.withDefaultFontFamily(defaultFontFamily),
        text2 = text2.withDefaultFontFamily(defaultFontFamily),
        button = button.withDefaultFontFamily(defaultFontFamily)
    )
}

// TODO: add shaped for required material2 components (dialogs and etc)
fun AppTypography.toMaterialTypography(): Typography {
    return Typography(
        button = button
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