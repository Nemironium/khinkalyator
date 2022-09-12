package me.nemiron.khinkalyator.features.meets.session.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme

@Composable
fun MeetSessionDetailsUi(
    component: MeetSessionDetailsComponent,
    modifier: Modifier = Modifier
) {

}

private class PreviewMeetSessionDetailsComponent : MeetSessionDetailsComponent

@Preview
@Composable
private fun MeetSessionDetailsPreview() {
    KhinkalyatorTheme {
        MeetSessionDetailsUi(PreviewMeetSessionDetailsComponent())
    }
}