package me.nemiron.khinkalyator.features.meets.home_page.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.widgets.InitialsBlock
import me.nemiron.khinkalyator.features.meets.home_page.ui.MeetHomePageViewData

@Composable
fun MeetHomePageCardPlaceholder(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 16.dp)
            .placeholder(
                visible = isVisible,
                shape = MaterialTheme.appShapes.card,
                highlight = PlaceholderHighlight.fade(),
            )
    )
}

@Composable
fun MeetHomePageCard(
    data: MeetHomePageViewData,
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
    data: MeetHomePageViewData,
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
    data: MeetHomePageViewData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        if (!data.isActive) {
            Text(
                text = stringResource(id = R.string.meets_home_page_archived),
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
private fun MeetHomePageCardPreview() {
    KhinkalyatorTheme {
        val initialsData = listOf(
            InitialsViewData("КЕ", Emoji("🐨")),
            InitialsViewData("КВ", Emoji("🦄")),
            InitialsViewData("ЖВ", Emoji("🐰")),
            InitialsViewData("РЧ", Emoji("🐮")),
            InitialsViewData("ЭЗ", Emoji("🐼"))
        )
        val meetData = MeetHomePageViewData(
            id = 1L,
            date = LocalizedString.resource(R.string.meets_home_page_today_date),
            title = LocalizedString.raw("Каха"),
            subtitle = LocalizedString.raw("Рубинштейна, 24"),
            initials = initialsData,
            isActive = false
        )

        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            MeetHomePageCard(
                data = meetData,
                onCardClick = { }
            )
        }
    }
}