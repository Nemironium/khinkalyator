package me.nemiron.khinkalyator.features.meets.check.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme

@Composable
fun MeetCheckUi(
    component: MeetCheckComponent,
    modifier: Modifier = Modifier
) {

}

private class PreviewMeetCheckComponent : MeetCheckComponent

@Preview
@Composable
private fun MeetCheckPreview() {
    KhinkalyatorTheme {
        MeetCheckUi(PreviewMeetCheckComponent())
    }
}