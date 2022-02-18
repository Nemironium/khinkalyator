package me.nemiron.khinkalyator.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.utils.statusBar
import me.nemiron.khinkalyator.core.ui.widgets.Toolbar

@Composable
fun MainUi(
    component: MainComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Toolbar(
                title = "MainUi",
                navigationIcon = null,
                statusBarModifier = Modifier.statusBar(color = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        content = {

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