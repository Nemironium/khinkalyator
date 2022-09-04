package me.nemiron.khinkalyator.features.meets.page.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.page.widgets.MeetCard

@Composable
fun MeetsPageUi(
    component: MeetsPageComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.meets_add_button),
                onClick = component::onMeetAddClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            MeetsCards(component, fabButtonPadding = 100.dp)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MeetsCards(
    component: MeetsPageComponent,
    fabButtonPadding: Dp,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 24.dp, bottom = fabButtonPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = component.meetsViewData, key = { it.id }) { meet ->
            MeetCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateItemPlacement(),
                data = meet,
                onCardClick = { component.onMeetClick(meet.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetsPagePreview() {
    KhinkalyatorTheme {
        MeetsPageUi(PreviewMeetsPageComponent())
    }
}

class PreviewMeetsPageComponent : MeetsPageComponent {

    override val meetsViewData: List<MeetFullViewData> = emptyList()

    override fun onMeetAddClick() = Unit
    override fun onMeetClick(meetId: MeetId) = Unit
}