package me.nemiron.khinkalyator.root.ui.start

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import me.aartikov.sesame.dialog.DialogControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.widgets.dialog.AlertDialogData
import me.nemiron.khinkalyator.core.widgets.dialog.DialogResult
import me.nemiron.khinkalyator.features.onboarding.domain.InitOnBoardingDataUseCase
import me.nemiron.khinkalyator.features.onboarding.domain.IsNeedToShowOnboardingUseCase
import me.nemiron.khinkalyator.features.onboarding.domain.SetOnboardingShownUseCase

class RealStartComponent(
    componentContext: ComponentContext,
    private val onOutput: (StartComponent.Output) -> Unit,
    private val isNeedToShowOnboarding: IsNeedToShowOnboardingUseCase,
    private val setOnboardingShown: SetOnboardingShownUseCase,
    private val initOnBoardingData: InitOnBoardingDataUseCase,
) : StartComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    override val onBoardingPromptDialog = DialogControl<AlertDialogData, DialogResult>()

    override var isLoadingVisible by mutableStateOf(true)

    init {
        coroutineScope.launch {
            checkOnboarding()
        }
    }

    private suspend fun checkOnboarding() {
        if (isNeedToShowOnboarding()) {
            when (showOnboardingDialog()) {
                DialogResult.Confirm -> {
                    initOnBoardingData()
                    setOnboardingShown()
                }
                DialogResult.Cancel -> {
                    // nothing
                }
            }
        }
        onOutput(StartComponent.Output.HomeRequested)
    }

    private suspend fun showOnboardingDialog(): DialogResult {
        val data = AlertDialogData(
            title = LocalizedString.resource(R.string.start_onboarding_dialog_title),
            positiveButtonText = LocalizedString.resource(R.string.start_onboarding_dialog_positive_button),
            dismissButtonText = LocalizedString.resource(R.string.start_onboarding_dialog_negative_button),
            isCancelable = false
        )

        return onBoardingPromptDialog.showForResult(data) ?: DialogResult.Cancel
    }
}