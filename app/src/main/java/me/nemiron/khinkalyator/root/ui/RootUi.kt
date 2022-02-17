package me.nemiron.khinkalyator.root.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.nemiron.khinkalyator.core.ui.utils.LocalApplyDarkStatusBarIcons
import me.nemiron.khinkalyator.evening.ui.EveningUi
import me.nemiron.khinkalyator.main.ui.MainUi

@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = false) {
        SystemBarColors()
        Children(component.routerState, modifier) {
            when (val child = it.instance) {
                is RootComponent.Child.Evening -> EveningUi(child.component)
                is RootComponent.Child.Main -> MainUi(child.component)
            }
        }
    }
}

@Composable
private fun SystemBarColors() {
    val systemUiController = rememberSystemUiController()

    val surfaceColor = MaterialTheme.colors.surface

    val statusBarDarkContentEnabled = LocalApplyDarkStatusBarIcons.current.count() > 0

    LaunchedEffect(surfaceColor, statusBarDarkContentEnabled) {
        systemUiController.statusBarDarkContentEnabled = statusBarDarkContentEnabled
        systemUiController.setStatusBarColor(Color.Transparent)
        systemUiController.setNavigationBarColor(surfaceColor)
    }
}