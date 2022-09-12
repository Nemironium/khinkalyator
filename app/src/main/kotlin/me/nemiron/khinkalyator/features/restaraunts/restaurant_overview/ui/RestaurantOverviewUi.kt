package me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.createFakeChildStack
import me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui.RestaurantDishesUi
import me.nemiron.khinkalyator.features.restaraunts.details.ui.PreviewRestaurantDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.details.ui.RestaurantDetailsUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RestaurantOverviewUi(
    component: RestaurantOverviewComponent,
    modifier: Modifier = Modifier
) {
    Children(
        modifier = modifier,
        stack = component.childStackState,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is RestaurantOverviewComponent.Child.RestaurantDishes -> RestaurantDishesUi(instance.component)
            is RestaurantOverviewComponent.Child.RestaurantDetails -> RestaurantDetailsUi(instance.component)
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun RestaurantOverviewPreview() {
    KhinkalyatorTheme {
        RestaurantOverviewUi(PreviewRestaurantOverviewComponent())
    }
}

private class PreviewRestaurantOverviewComponent : RestaurantOverviewComponent {
    override val childStackState = createFakeChildStack(
        RestaurantOverviewComponent.Child.RestaurantDetails(PreviewRestaurantDetailsComponent())
    )
}