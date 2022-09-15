package me.nemiron.khinkalyator.root.ui.start

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.nemiron.khinkalyator.core.widgets.dialog.ShowAlertDialog

@Composable
fun StartUi(component: StartComponent, modifier: Modifier = Modifier) {
    // TODO: add progress loading
    ShowAlertDialog(dialogControl = component.onBoardingPromptDialog)
}