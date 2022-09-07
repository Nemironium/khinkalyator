package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.emoji.widgets.BigEmojiWithBackground

@Composable
fun ImageWithText(
    title: String,
    modifier: Modifier = Modifier,
    textPadding: PaddingValues = PaddingValues(),
    onClick: () -> Unit,
    imageContent: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.appShapes.card)
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        imageContent()
        Text(
            modifier = Modifier
                .padding(textPadding)
                .align(Alignment.CenterHorizontally),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.appTypography.text2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ImageWithTextPreview() {
    KhinkalyatorTheme {
        ImageWithText(
            title = "Павел",
            onClick = { }
        ) {
            BigEmojiWithBackground(
                emoji = Emoji("🐨"),
                isSelected = true
            )
        }
    }
}