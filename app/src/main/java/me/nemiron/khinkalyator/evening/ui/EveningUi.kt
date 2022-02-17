package me.nemiron.khinkalyator.evening.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.Toolbar

@Composable
fun EveningUi(
    component: EveningComponent,
    modifier: Modifier = Modifier
) {
    Toolbar(title = "EveningUi")
}

@Preview(showBackground = true)
@Composable
private fun EveningUiPreview() {
    KhinkalyatorTheme {
        EveningUi(PreviewEveningComponent())
    }
}

private class PreviewEveningComponent : EveningComponent