package me.nemiron.khinkalyator.root.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.LocalApplyDarkStatusBarIcons
import me.nemiron.khinkalyator.core.utils.createFakeChildStack
import me.nemiron.khinkalyator.features.home.ui.HomeUi
import me.nemiron.khinkalyator.features.home.ui.PreviewHomeComponent
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetUi
import me.nemiron.khinkalyator.features.meets.overview.ui.MeetOverviewUi
import me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui.RestaurantOverviewUi
import me.nemiron.khinkalyator.root.ui.start.StartUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    SystemBarColors()
    Children(
        stack = component.childStack,
        animation = stackAnimation { child: Child.Created<Any, RootComponent.Child>, _, _ ->
            when (child.instance) {
                is RootComponent.Child.Meet, is RootComponent.Child.Restaurant -> slide()
                is RootComponent.Child.Start, is RootComponent.Child.Home, is RootComponent.Child.CreateMeet -> fade()
            }
        },
        modifier = modifier
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Start -> StartUi(child.component)
            is RootComponent.Child.Home -> HomeUi(child.component)
            is RootComponent.Child.CreateMeet -> CreateMeetUi(child.component)
            is RootComponent.Child.Meet -> MeetOverviewUi(child.component)
            is RootComponent.Child.Restaurant -> RestaurantOverviewUi(child.component)
        }
    }
}

@Composable
private fun SystemBarColors() {
    val systemUiController = rememberSystemUiController()

    val backgroundColor = MaterialTheme.colors.background

    val statusBarDarkContentEnabled = LocalApplyDarkStatusBarIcons.current.size > 0

    LaunchedEffect(backgroundColor, statusBarDarkContentEnabled) {
        systemUiController.setStatusBarColor(Color.Transparent)
        systemUiController.setNavigationBarColor(backgroundColor)
        systemUiController.statusBarDarkContentEnabled = statusBarDarkContentEnabled
    }
}

@Preview(showBackground = true)
@Composable
private fun RootPreview() {
    KhinkalyatorTheme {
        RootUi(PreviewRootComponent())
    }
}

private class PreviewRootComponent : RootComponent {
    override val childStack =
        createFakeChildStack(RootComponent.Child.Home(PreviewHomeComponent()))
}