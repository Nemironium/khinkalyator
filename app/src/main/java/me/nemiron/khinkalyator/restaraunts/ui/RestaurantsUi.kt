package me.nemiron.khinkalyator.restaraunts.ui

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
fun RestaurantsUi(
    component: RestaurantsComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.restaurants_add),
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
private fun RestaurantsUiPreview() {
    KhinkalyatorTheme {
        RestaurantsUi(PreviewRestaurantsComponent())
    }
}

class PreviewRestaurantsComponent : RestaurantsComponent