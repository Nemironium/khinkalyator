package me.nemiron.khinkalyator.features.meets.create.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.ImageWithText
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.features.emoji.widgets.BigEmojiWithBackground
import me.nemiron.khinkalyator.features.meets.create.ui.PersonSimpleViewData

@Composable
fun MediumPersonItem(
    data: PersonSimpleViewData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ImageWithText(
        modifier = modifier,
        textPadding = PaddingValues(start = 4.dp, end = 4.dp, top = 8.dp),
        title = data.title.resolve(),
        onClick = onClick
    ) {
        BigEmojiWithBackground(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            emoji = data.emoji,
            isSelected = data.isSelected
        )
    }
}

@Preview
@Composable
private fun MediumPersonItemPreview() {
    val data = PersonSimpleViewData(
        id = -1,
        title = LocalizedString.raw("Павел"),
        emoji = Emoji("🐨"),
        isSelected = true
    )
    KhinkalyatorTheme {
        Box(Modifier.padding(16.dp)) {
            MediumPersonItem(
                modifier = Modifier.size(width = 84.dp, height = 110.dp),
                data = data,
                onClick = {}
            )
        }
    }
}