package me.nemiron.khinkalyator.features.meets.overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.createFakeChildStack
import me.nemiron.khinkalyator.features.meets.details.ui.MeetDetailsUi
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetPagerUi
import me.nemiron.khinkalyator.features.meets.pager.ui.PreviewMeetPagerComponent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MeetOverviewUi(
    component: MeetOverviewComponent,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier,
        stack = component.childStack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is MeetOverviewComponent.Child.Details -> MeetDetailsUi(instance.component)
            is MeetOverviewComponent.Child.Pager -> MeetPagerUi(instance.component)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetOverviewPreview() {
    KhinkalyatorTheme {
        MeetOverviewUi(PreviewMeetOverviewComponent())
    }
}

private class PreviewMeetOverviewComponent : MeetOverviewComponent {
    override val childStack = createFakeChildStack(
        MeetOverviewComponent.Child.Pager(PreviewMeetPagerComponent())
    )
    override val checkComponent = null
    override fun onCheckDismissed() = Unit
}