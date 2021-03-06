package me.nemiron.khinkalyator.root.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.LocalApplyDarkStatusBarIcons
import me.nemiron.khinkalyator.core.utils.createFakeRouterState
import me.nemiron.khinkalyator.features.home.ui.HomeUi
import me.nemiron.khinkalyator.features.home.ui.PreviewHomeComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantUi

@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    SystemBarColors()
    Children(component.routerState, modifier) {
        when (val child = it.instance) {
            is RootComponent.Child.Home -> HomeUi(child.component)
            is RootComponent.Child.Restaurant -> RestaurantUi(child.component)
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
private fun RootUiPreview() {
    KhinkalyatorTheme {
        RootUi(PreviewRootComponent())
    }
}

private class PreviewRootComponent : RootComponent {
    override val routerState =
        createFakeRouterState(RootComponent.Child.Home(PreviewHomeComponent()))
}