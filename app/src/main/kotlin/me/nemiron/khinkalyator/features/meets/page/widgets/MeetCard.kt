package me.nemiron.khinkalyator.features.meets.page.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.widgets.InitialsBlock
import me.nemiron.khinkalyator.features.meets.page.ui.MeetFullViewData

@Composable
fun MeetCard(
    data: MeetFullViewData,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val border = if (data.isActive) {
        BorderStroke(2.dp, MaterialTheme.colors.primary)
    } else {
        null
    }

    Card(
        modifier = modifier.height(130.dp),
        border = border,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onCardClick() }
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 20.dp)
        ) {
            InfoBlock(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                data = data
            )
            PeopleBlock(data = data)
        }
    }
}

@Composable
private fun InfoBlock(
    data: MeetFullViewData,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = data.date.resolve(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.appTypography.text2,
            color = MaterialTheme.additionalColors.secondaryOnSurface
        )
        Spacer(Modifier.weight(0.4f))
        Text(
            text = data.title.resolve(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.appTypography.head2
        )
        Spacer(Modifier.weight(0.6f))
        Text(
            text = data.subtitle.resolve(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.appTypography.text2,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun PeopleBlock(
    data: MeetFullViewData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        if (!data.isActive) {
            Text(
                text = stringResource(id = R.string.meets_archived),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.appTypography.text2,
                color = MaterialTheme.additionalColors.secondaryOnSurface
            )
        }
        Spacer(Modifier.weight(1f))
        InitialsBlock(data.initials, visibleCount = 4)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MeetCardPreview() {
    KhinkalyatorTheme {
        val initialsData = listOf(
            InitialsViewData("????", Emoji("????")),
            InitialsViewData("????", Emoji("????")),
            InitialsViewData("????", Emoji("????")),
            InitialsViewData("????", Emoji("????")),
            InitialsViewData("????", Emoji("????"))
        )
        val meetData = MeetFullViewData(
            id = 1L,
            date = LocalizedString.resource(R.string.meets_today_date),
            title = LocalizedString.raw("????????"),
            subtitle = LocalizedString.raw("??????????????????????, 24"),
            initials = initialsData,
            isActive = false
        )

        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            MeetCard(
                data = meetData,
                onCardClick = { }
            )
        }
    }
}