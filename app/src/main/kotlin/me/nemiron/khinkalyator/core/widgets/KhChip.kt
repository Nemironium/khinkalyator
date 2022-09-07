package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography

@Composable
fun KhChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    isSelected: Boolean = false
) {
    KhChipImpl(
        modifier = modifier,
        paddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        onClick = onClick,
        elevation = elevation,
        isSelected = isSelected
    ) {
        Text(text = text, style = MaterialTheme.appTypography.text1)
    }
}

@Composable
fun KhChip(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp
) {
    KhChipImpl(
        modifier = modifier,
        paddingValues = PaddingValues(4.dp),
        onClick = onClick,
        elevation = elevation,
        isSelected = false
    ) {
        Image(
            painter = painter,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun KhChipImpl(
    onClick: () -> Unit,
    elevation: Dp,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    content: @Composable BoxScope.() -> Unit
) {
    val color = if (isSelected) {
        MaterialTheme.additionalColors.secondaryContainer
    } else {
        MaterialTheme.colors.surface
    }
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.appShapes.chip,
        color = color,
        elevation = elevation
    ) {
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Preview
@Composable
private fun KhChipsPreview() {
    KhinkalyatorTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KhChip(
                text = "Хачапури по-аджарски",
                isSelected = true,
                onClick = { }
            )
            KhChip(
                painter = painterResource(R.drawable.ic_plus_32),
                onClick = { }
            )
            KhChip(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.ic_plus_32),
                onClick = { }
            )
        }
    }
}