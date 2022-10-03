package me.nemiron.khinkalyator.features.meets.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme

@Composable
fun MeetDetailsUi(
    component: MeetDetailsComponent,
    modifier: Modifier = Modifier
) {

}

private class PreviewMeetDetailsComponent : MeetDetailsComponent

@Preview
@Composable
private fun MeetDetailsPreview() {
    KhinkalyatorTheme {
        MeetDetailsUi(PreviewMeetDetailsComponent())
    }
}