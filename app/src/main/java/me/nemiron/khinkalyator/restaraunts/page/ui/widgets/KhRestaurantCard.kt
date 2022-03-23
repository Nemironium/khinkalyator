package me.nemiron.khinkalyator.restaraunts.page.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import me.nemiron.khinkalyator.core.ui.utils.resolve
import me.nemiron.khinkalyator.core.ui.widgets.IconWithBackground
import me.nemiron.khinkalyator.restaraunts.page.ui.RestaurantFullViewData

// FIXME: make correct text overflow with long address
@Composable
fun KhRestaurantCard(
    data: RestaurantFullViewData,
    onCardClick: () -> Unit,
    onCallClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardClick() }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = data.title.resolve(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.head2
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = data.subtitle.resolve(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.text2
                )
            }

            IconActions(
                modifier = Modifier
                    .padding(bottom = 20.dp, end = 20.dp)
                    .align(Alignment.BottomEnd),
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
            IconWithBackground(
                painter = painterResource(R.drawable.ic_phone_40),
                clickableModifier = Modifier.clickable { onCallClick() }
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        IconWithBackground(
            painter = painterResource(R.drawable.ic_share_40),
            clickableModifier = Modifier.clickable { onShareClick() }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun KhRestaurantCardPreview() {
    KhinkalyatorTheme {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            KhRestaurantCard(
                modifier = Modifier
                    .fillMaxWidth(),
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