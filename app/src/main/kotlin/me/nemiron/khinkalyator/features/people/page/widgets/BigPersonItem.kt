package me.nemiron.khinkalyator.features.people.page.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.emoji.widgets.BigEmojiWithBackground
import me.nemiron.khinkalyator.features.people.page.ui.PersonFullViewData

@Composable
fun BigPersonItem(
    data: PersonFullViewData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        BigEmojiWithBackground(
            modifier = Modifier.align(Alignment.CenterVertically),
            emoji = data.emoji
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data.name.resolve(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.appTypography.medium
            )

            data.phone?.let {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = data.phone.resolve(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.text2
                )
            }
        }
    }
}

@Preview
@Composable
fun BigPersonItemsPreview() {
    KhinkalyatorTheme {
        Column {
            BigPersonItem(
                data = PersonFullViewData(
                    id = 1L,
                    name = LocalizedString.raw("Томочка Тараненко"),
                    phone = LocalizedString.raw("8 (999) 385 - 23 - 91"),
                    emoji = Emoji("🦄")
                ),
                onClick = { }
            )

            BigPersonItem(
                data = PersonFullViewData(
                    id = 1L,
                    name = LocalizedString.raw("Жека Кауров"),
                    phone = null,
                    emoji = Emoji("🐨")
                ),
                onClick = { }
            )
        }
    }
}