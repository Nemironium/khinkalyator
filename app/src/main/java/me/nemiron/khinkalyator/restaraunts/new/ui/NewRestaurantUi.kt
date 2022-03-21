package me.nemiron.khinkalyator.restaraunts.new.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.ui.widgets.KhToolbar

@Composable
fun NewRestaurantUi(
    component: NewRestaurantComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KhToolbar(title = stringResource(id = R.string.new_restaurant_title))
        },
        content = { contentPadding ->
            KhContainedButton(
                modifier = Modifier.padding(contentPadding),
                iconRes = R.drawable.img_khinkali_double,
                onClick = component::onDeleteRestaurantClick
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NewRestaurantUiPreview() {
    KhinkalyatorTheme {
        NewRestaurantUi(PreviewNewRestaurantComponent())
    }
}

private class PreviewNewRestaurantComponent : NewRestaurantComponent {
    override fun onDeleteRestaurantClick() = Unit
}