package me.nemiron.khinkalyator.features.meets.meet_session_check.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme

@Composable
fun MeetSessionCheckUi(
    component: MeetSessionCheckComponent,
    modifier: Modifier = Modifier
) {

}

private class PreviewMeetSessionCheckComponent : MeetSessionCheckComponent

@Preview
@Composable
private fun MeetSessionCheckPreview() {
    KhinkalyatorTheme {
        MeetSessionCheckUi(PreviewMeetSessionCheckComponent())
    }
}