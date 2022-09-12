package me.nemiron.khinkalyator.features.dishes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.KhChip
import me.nemiron.khinkalyator.features.dishes.domain.Dish
import me.nemiron.khinkalyator.features.dishes.domain.DishId

@Composable
fun DishesUi(
    component: DishesComponent,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.dishes_title),
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.appTypography.head3
        )
        Spacer(Modifier.height(16.dp))
        // TODO: if selected element not on screen, then row must be scrolled
        FlowRow(
            modifier = Modifier.padding(bottom = 8.dp),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            if (component.dishesViewData.isNotEmpty()) {
                KhChip(
                    painter = painterResource(R.drawable.ic_plus_32),
                    onClick = component::onDishAddClick
                )
            }
            component.dishesViewData.forEach { dish ->
                KhChip(
                    text = dish.title.resolve(),
                    isSelected = dish.isSelected,
                    onClick = { component.onDishClick(dish.id) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DishesPreview() {
    KhinkalyatorTheme {
        DishesUi(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.additionalColors.secondaryBackground),
            component = PreviewDishesComponent()
        )
    }
}

class PreviewDishesComponent : DishesComponent {
    override val dishesViewData = Dish.MOCKS.map(Dish::toDishViewData)
    override fun onDishAddClick() = Unit
    override fun onDishClick(dishId: DishId) = Unit
}