package me.nemiron.khinkalyator.features.meets.meet.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.core.widgets.KhToolbar

@Composable
fun MeetUi(
    component: MeetComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KhToolbar(
                title = "Заголовок",
                actionIcon = {

                }
            )
        },
        content = { contentPadding ->

        }
    )
}