package me.nemiron.khinkalyator.meets.ui

import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton

@Composable
fun MeetsUi(
    component: MeetsComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.meets_add),
                onClick = { }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            KhContainedButton(
                modifier = Modifier,
                iconRes = R.drawable.img_khinkali_double,
                onClick = { }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MeetsUiPreview() {
    KhinkalyatorTheme {
        MeetsUi(PreviewMeetsComponent())
    }
}

class PreviewMeetsComponent : MeetsComponent