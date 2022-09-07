package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography

@Composable
fun KhOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KhOutlinedButtonImpl(
        modifier = modifier,
        onClick = onClick,
        contentColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 24.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.appTypography.button,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun KhOutlinedButtonImpl(
    onClick: () -> Unit,
    contentColor: Color,
    backgroundColor: Color,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = MaterialTheme.appShapes.button,
        border = BorderStroke(0.dp, Color.Transparent),
        contentPadding = contentPadding,
        content = { content() }
    )
}

@Preview
@Composable
private fun KhOutlinedButtonPreview() {
    KhinkalyatorTheme {
        KhOutlinedButton(
            text = "Добавить встречу",
            onClick = { }
        )
    }
}