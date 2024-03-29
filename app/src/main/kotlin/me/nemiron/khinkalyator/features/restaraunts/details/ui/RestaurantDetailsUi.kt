package me.nemiron.khinkalyator.features.restaraunts.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.ui.Scaffold
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.TextTransformations
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.IconWithBackground
import me.nemiron.khinkalyator.core.widgets.KhChip
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.widgets.KhOutlinedTextField
import me.nemiron.khinkalyator.core.widgets.KhToolbar
import me.nemiron.khinkalyator.core.widgets.OverflowMenu
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.DishViewData

// FIXME: Screen doesn't properly adapt Composables sizes after screenSize config changes
@Composable
fun RestaurantDetailsUi(
    component: RestaurantDetailsComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KhToolbar(
                title = component.title.resolve(),
                actionIcon = {
                    component.menuData?.let {
                        OverflowMenu(it)
                    }
                }
            )
        },
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier.navigationBarsPadding(),
                text = stringResource(R.string.restaurant_details_submit_button),
                onClick = component::onSubmitClick,
                enabled = component.isButtonActive
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                RestaurantTextFields(
                    nameInputControl = component.nameInputControl,
                    addressInputControl = component.addressInputControl,
                    phoneInputControl = component.phoneInputControl
                )
                Spacer(Modifier.height(32.dp))
                DishesContent(
                    dishes = component.dishesViewData,
                    fabButtonPadding = 120.dp,
                    onDishAddClick = component::onDishAddClick,
                    onDishClick = component::onDishClick
                )
            }
        }
    )
}

@Composable
private fun RestaurantTextFields(
    nameInputControl: InputControl,
    addressInputControl: InputControl,
    phoneInputControl: InputControl
) {
    KhOutlinedTextField(
        inputControl = nameInputControl,
        placeholder = stringResource(R.string.restaurant_details_name_placeholder),
        leadingIcon = {
            IconWithBackground(
                painter = painterResource(R.drawable.ic_restaurant_32),
                backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
            )
        }
    )
    Spacer(Modifier.height(16.dp))
    KhOutlinedTextField(
        inputControl = addressInputControl,
        placeholder = stringResource(R.string.restaurant_details_address_placeholder),
        leadingIcon = {
            IconWithBackground(
                painter = painterResource(R.drawable.ic_location_32),
                backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
            )
        }
    )
    Spacer(Modifier.height(16.dp))
    KhOutlinedTextField(
        inputControl = phoneInputControl,
        placeholder = stringResource(R.string.restaurant_details_phone_placeholder),
        leadingIcon = {
            IconWithBackground(
                painter = painterResource(R.drawable.ic_phone_32),
                backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
            )
        }
    )
}

@Composable
private fun ColumnScope.DishesContent(
    dishes: List<DishViewData>,
    fabButtonPadding: Dp,
    onDishAddClick: () -> Unit,
    onDishClick: (id: DishId) -> Unit
) {
    Text(
        modifier = Modifier.align(Alignment.Start),
        text = stringResource(R.string.restaurant_details_dishes_title),
        style = MaterialTheme.appTypography.head3
    )
    Spacer(Modifier.height(16.dp))
    val addDishButtonModifier = if (dishes.isEmpty()) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
    }
    FlowRow(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(bottom = 8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        KhChip(
            modifier = addDishButtonModifier,
            painter = painterResource(R.drawable.ic_plus_32),
            onClick = onDishAddClick
        )
        dishes.forEach { dish ->
            KhChip(
                text = dish.title.resolve(),
                onClick = { onDishClick(dish.id) }
            )
        }
        Spacer(Modifier.height(fabButtonPadding))
    }
}

@Preview(showBackground = true)
@Composable
private fun RestaurantPreview() {
    KhinkalyatorTheme {
        RestaurantDetailsUi(PreviewRestaurantDetailsComponent())
    }
}

class PreviewRestaurantDetailsComponent : RestaurantDetailsComponent {

    override val nameInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    override val phoneInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Phone
        ),
        textTransformation = TextTransformations.PhoneNumber
    )

    override val addressInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    override val dishesViewData: List<DishViewData> = listOf()
    override val title = LocalizedString.resource(R.string.restaurant_details_title_new)
    override val isButtonActive = nameInputControl.text.isNotBlank()
    override val menuData = null

    override fun onSubmitClick() = Unit
    override fun onDishAddClick() = Unit
    override fun onDishClick(dishId: DishId) = Unit
}