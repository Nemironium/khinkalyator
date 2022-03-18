package me.nemiron.khinkalyator.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.ui.widgets.KhOutlinedButton
import me.nemiron.khinkalyator.core.ui.widgets.Toolbar

@Composable
fun MainUi(
    component: MainComponent,
    modifier: Modifier = Modifier
) {
    // FIXME: почему-то content начинается аж от SystemBar, а не под topBar
    Scaffold(
        topBar = {
            Toolbar(
                title = "MainUi",
                navigationIcon = null
            )
        },
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                iconRes = R.drawable.img_khinkali_double,
                onClick = { }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            KhOutlinedButton(
                modifier = Modifier.padding(top = 80.dp),
                text = "Добавить встречу",
                onClick = { }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MainUiPreview() {
    KhinkalyatorTheme {
        MainUi(PreviewMainComponent())
    }
}

class PreviewMainComponent : MainComponent