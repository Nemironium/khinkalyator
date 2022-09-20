package me.nemiron.khinkalyator.root.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.statusBar
import me.nemiron.khinkalyator.core.widgets.KhContainedButtonPlaceholder
import me.nemiron.khinkalyator.core.widgets.KhTabRowPlaceholder
import me.nemiron.khinkalyator.core.widgets.dialog.ShowAlertDialog
import me.nemiron.khinkalyator.features.meets.home_page.widgets.MeetHomePageCardPlaceholder

@Composable
fun StartUi(component: StartComponent, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.statusBar(MaterialTheme.colors.primary),
        topBar = {
            KhTabRowPlaceholder(isVisible = component.isLoadingVisible)
        },
        floatingActionButton = {
            KhContainedButtonPlaceholder(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = component.isLoadingVisible,
                text = stringResource(R.string.meets_add_button)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(3) {
                    MeetHomePageCardPlaceholder(isVisible = component.isLoadingVisible)
                }
            }
        }
    )
    ShowAlertDialog(dialogControl = component.onBoardingPromptDialog)
}