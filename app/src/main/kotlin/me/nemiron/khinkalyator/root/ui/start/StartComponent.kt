package me.nemiron.khinkalyator.root.ui.start

import me.aartikov.sesame.dialog.DialogControl
import me.nemiron.khinkalyator.core.widgets.dialog.AlertDialogData
import me.nemiron.khinkalyator.core.widgets.dialog.DialogResult

interface StartComponent {
    val onBoardingPromptDialog: DialogControl<AlertDialogData, DialogResult>
    val isLoadingVisible: Boolean

    sealed interface Output {
        object HomeRequested : Output
    }
}