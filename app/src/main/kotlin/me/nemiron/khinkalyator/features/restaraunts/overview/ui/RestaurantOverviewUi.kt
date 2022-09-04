package me.nemiron.khinkalyator.features.restaraunts.overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.createFakeChildStack
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.MenuDetailsUi
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.PreviewRestaurantDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantDetailsUi

@Composable
fun RestaurantOverviewUi(
    component: RestaurantOverviewComponent,
    modifier: Modifier = Modifier
) {
    Children(component.childStackState, modifier) { child ->
        when (val instance = child.instance) {
            is RestaurantOverviewComponent.Child.MenuDetails -> MenuDetailsUi(instance.component)
            is RestaurantOverviewComponent.Child.RestaurantDetails -> RestaurantDetailsUi(instance.component)
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun RestaurantOverviewUiPreview() {
    KhinkalyatorTheme {
        RestaurantOverviewUi(PreviewRestaurantOverviewComponent())
    }
}

private class PreviewRestaurantOverviewComponent : RestaurantOverviewComponent {
    override val childStackState = createFakeChildStack(
        RestaurantOverviewComponent.Child.RestaurantDetails(PreviewRestaurantDetailsComponent())
    )
}