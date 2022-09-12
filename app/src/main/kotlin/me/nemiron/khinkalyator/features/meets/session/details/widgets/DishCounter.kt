package me.nemiron.khinkalyator.features.meets.session.details.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.widgets.IconWithBackground

// TODO: bound to own Decompose component
@Composable
fun DishCounter(
    text: String,
    textColor: Color,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.appShapes.chip)
            .background(MaterialTheme.colors.surface)
            .border(
                width = 1.dp,
                shape = MaterialTheme.appShapes.chip,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
            )
            .padding(4.dp)
    ) {
        IconWithBackground(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(end = 4.dp),
            painter = painterResource(R.drawable.ic_minus_40),
            // FIXME: add color to library
            backgroundColor = Color(0xFFC7C7C7),
            contentColor = MaterialTheme.colors.onBackground,
            onClick = onMinusClick
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = textColor,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        IconWithBackground(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 4.dp),
            painter = painterResource(R.drawable.ic_plus_40),
            backgroundColor = MaterialTheme.colors.secondary,
            onClick = onPlusClick
        )
    }
}

@Preview
@Composable
private fun DishCounterPreview() {
    KhinkalyatorTheme {
        DishCounter(
            text = "3 шт, 900 ₽",
            textColor = Color.Black,
            onMinusClick = {},
            onPlusClick = {}
        )
    }
}