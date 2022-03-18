package me.nemiron.khinkalyator.people.ui

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
fun PeopleUi(
    component: PeopleComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.people_add),
                onClick = { }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {

        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PeopleUiPreview() {
    KhinkalyatorTheme {
        PeopleUi(PreviewPeopleComponent())
    }
}

private class PreviewPeopleComponent : PeopleComponent