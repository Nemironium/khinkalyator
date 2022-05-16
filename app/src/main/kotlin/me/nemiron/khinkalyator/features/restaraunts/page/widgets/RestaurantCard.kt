package me.nemiron.khinkalyator.features.restaraunts.page.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.ui.widgets.IconWithBackground
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantFullViewData

@Composable
fun RestaurantCard(
    data: RestaurantFullViewData,
    onCardClick: () -> Unit,
    onCallClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onCardClick() }
                .padding(start = 24.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = data.title.resolve(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.head2
                )
                Spacer(Modifier.weight(1.0f))
                Text(
                    text = data.subtitle.resolve(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.text2
                )
            }

            IconActions(
                modifier = Modifier.align(Alignment.Bottom),
                phoneIconVisible = data.phoneIconVisible,
                onCallClick = onCallClick,
                onShareClick = onShareClick
            )
        }
    }
}

@Composable
private fun IconActions(
    phoneIconVisible: Boolean,
    onCallClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        if (phoneIconVisible) {
            IconWithBackground(painterResource(R.drawable.ic_phone_40)) {
                onCallClick()
            }
            Spacer(Modifier.width(12.dp))
        }

        IconWithBackground(painterResource(R.drawable.ic_share_40)) {
            onShareClick()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RestaurantCardPreview() {
    KhinkalyatorTheme {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            RestaurantCard(
                data = RestaurantFullViewData(
                    id = 1L,
                    title = LocalizedString.raw("Каха"),
                    subtitle = LocalizedString.raw("Рубинштейна, 24"),
                    phoneIconVisible = true
                ),
                onCardClick = { },
                onCallClick = { },
                onShareClick = { }
            )
        }
    }
}