package me.nemiron.khinkalyator.core.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography

@Composable
fun KhContainedButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KhButtonImpl(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(
            vertical = 4.dp,
            horizontal = 16.dp
        ),
        enabled = true
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
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    KhButtonImpl(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 24.dp
        ),
        enabled = enabled
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
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            disabledBackgroundColor = MaterialTheme.additionalColors.secondaryContainer,
            disabledContentColor = MaterialTheme.colors.onSecondary,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        shape = MaterialTheme.appShapes.button,
        contentPadding = contentPadding,
        content = { content() }
    )
}

@Preview
@Composable
private fun KhContainedButtonPreview() {
    KhinkalyatorTheme {
        KhContainedButton(
            text = "Добавить встречу",
            onClick = { }
        )
    }
}