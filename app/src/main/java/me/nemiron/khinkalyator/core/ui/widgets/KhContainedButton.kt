package me.nemiron.khinkalyator.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.theme.appShapes
import me.nemiron.khinkalyator.core.ui.theme.appTypography

@Composable
fun KhContainedButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KhButtonImpl(
        modifier = modifier,
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary,
        contentPadding = PaddingValues(
            vertical = 4.dp,
            horizontal = 16.dp
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null
        )
    }
}

@Composable
fun KhContainedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KhButtonImpl(
        modifier = modifier,
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary,
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 24.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.appTypography.button,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun KhButtonImpl(
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = MaterialTheme.appShapes.button,
        contentPadding = contentPadding,
        content = { content() }
    )
}

@Preview
@Composable
fun KhContainedButtonPreview() {
    KhinkalyatorTheme {
        KhContainedButton(
            text = "Добавить встречу",
            onClick = { }
        )
    }
}