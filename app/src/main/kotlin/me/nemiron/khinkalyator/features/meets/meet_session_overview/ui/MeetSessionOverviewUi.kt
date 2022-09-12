package me.nemiron.khinkalyator.features.meets.meet_session_overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.createFakeChildStack
import me.nemiron.khinkalyator.features.meets.meet_session_details.ui.MeetSessionDetailsUi
import me.nemiron.khinkalyator.features.meets.meet_session_pager.ui.MeetSessionPagerUi
import me.nemiron.khinkalyator.features.meets.meet_session_pager.ui.PreviewMeetSessionPagerComponent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MeetSessionOverviewUi(
    component: MeetSessionOverviewComponent,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier,
        stack = component.childStackState,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is MeetSessionOverviewComponent.Child.Details -> MeetSessionDetailsUi(instance.component)
            is MeetSessionOverviewComponent.Child.Pager -> MeetSessionPagerUi(instance.component)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetSessionOverviewPreview() {
    KhinkalyatorTheme {
        MeetSessionOverviewUi(PreviewMeetSessionOverviewComponent())
    }
}

private class PreviewMeetSessionOverviewComponent : MeetSessionOverviewComponent {
    override val childStackState = createFakeChildStack(
        MeetSessionOverviewComponent.Child.Pager(PreviewMeetSessionPagerComponent())
    )
    override val checkComponent = null
    override fun onCheckDismissed() = Unit
}