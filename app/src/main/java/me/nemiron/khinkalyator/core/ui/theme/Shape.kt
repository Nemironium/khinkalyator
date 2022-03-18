package me.nemiron.khinkalyator.core.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

@Immutable
data class AppShapes(
    val button: CornerBasedShape = RoundedCornerShape(100.dp),
    val card: CornerBasedShape = RoundedCornerShape(16.dp)
)

fun AppShapes.toMaterialShapes(): Shapes {
    return Shapes(
        small = button,
        medium = card
    )
}

val LocalAppShapes = staticCompositionLocalOf {
    AppShapes()
}

val MaterialTheme.appShapes: AppShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalAppShapes.current