package me.nemiron.khinkalyator.features.restaraunts.page.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantId
import me.nemiron.khinkalyator.features.restaraunts.page.ui.widgets.KhRestaurantCard

@Composable
fun RestaurantsPageUi(
    component: RestaurantsPageComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.restaurants_add),
                onClick = component::onRestaurantAddClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            RestaurantCards(component, fabButtonPadding = 100.dp)
        }
    )
}

@Composable
private fun RestaurantCards(
    component: RestaurantsPageComponent,
    fabButtonPadding: Dp,
    modifier: Modifier = Modifier
) {
    // FIXME: add ItemPlacement animation
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 24.dp, bottom = fabButtonPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = component.restaurantsViewData, key = { it.id }) { restaurant ->
            KhRestaurantCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                data = restaurant,
                onCardClick = { component.onRestaurantClick(restaurant.id) },
                onCallClick = { component.onRestaurantCallClick(restaurant.id) },
                onShareClick = { component.onRestaurantShareClick(restaurant.id) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RestaurantsPageUiPreview() {
    KhinkalyatorTheme {
        RestaurantsPageUi(PreviewRestaurantsPageComponent())
    }
}

class PreviewRestaurantsPageComponent : RestaurantsPageComponent {

    override val restaurantsViewData: List<RestaurantFullViewData> = emptyList()

    override fun onRestaurantAddClick() = Unit
    override fun onRestaurantClick(restaurantId: RestaurantId) = Unit
    override fun onRestaurantCallClick(restaurantId: RestaurantId) = Unit
    override fun onRestaurantShareClick(restaurantId: RestaurantId) = Unit
}