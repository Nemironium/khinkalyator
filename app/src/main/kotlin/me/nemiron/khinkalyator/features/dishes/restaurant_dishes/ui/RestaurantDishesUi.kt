package me.nemiron.khinkalyator.features.dishes.restaurant_dishes.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.consumeOnTapGestures
import me.nemiron.khinkalyator.core.utils.dismissOnTapOutsideElements
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.*
import me.nemiron.khinkalyator.features.dishes.ui.DishesUi
import me.nemiron.khinkalyator.features.dishes.ui.PreviewDishesComponent

@Composable
fun RestaurantDishesUi(
    component: RestaurantDishesComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.dismissOnTapOutsideElements(),
        backgroundColor = MaterialTheme.additionalColors.secondaryBackground,
        topBar = {
            KhModalToolbar(
                title = component.topTitle.resolve(),
                logoIcon = {
                    IconWithBackground(
                        modifier = Modifier.padding(start = 16.dp),
                        painter = painterResource(R.drawable.ic_restaurant_32),
                        backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer,
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                val modalPadding = 248.dp
                DishesUi(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = modalPadding)
                        .verticalScroll(rememberScrollState()),
                    component = component.dishesComponent
                )
                DishModal(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        /* to prevent dismissing after sheet touching */
                        .consumeOnTapGestures()
                        .imePadding(),
                    title = component.bottomTitle,
                    isDeleteVisible = component.isDeleteActionVisible,
                    nameInputControl = component.nameInputControl,
                    priceInputControl = component.priceInputControl,
                    onDishDeleteClick = component::onDishDeleteClick,
                    onDishNextClick = component::onDishNextClick,
                    onSubmitClick = component::onSubmitClick
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DishModal(
    title: LocalizedString,
    isDeleteVisible: Boolean,
    nameInputControl: InputControl,
    priceInputControl: InputControl,
    onDishDeleteClick: () -> Unit,
    onDishNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                MaterialTheme.appShapes.sheet.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colors.background)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Column {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, bottom = 8.dp, end = 12.dp),
                    text = title.resolve(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.appTypography.medium
                )
                AnimatedVisibility(
                    visible = isDeleteVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconWithBackground(
                        painter = painterResource(R.drawable.ic_delete_32),
                        backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer,
                        onClick = onDishDeleteClick
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KhOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.65f),
                    inputControl = nameInputControl,
                    placeholder = stringResource(R.string.restaurant_dishes_name_placeholder),
                    leadingIcon = {
                        IconWithBackground(
                            painter = painterResource(R.drawable.ic_food_32),
                            backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
                        )
                    }
                )
                Spacer(Modifier.width(8.dp))
                KhOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.35f),
                    inputControl = priceInputControl,
                    placeholder = stringResource(R.string.restaurant_dishes_price_placeholder),
                    trailingIcon = {
                        Text(
                            text = stringResource(R.string.common_currency_symbol),
                            color = Color.Black
                        )
                    },
                    onAction = { keyboardController?.hide() }
                )
            }
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KhOutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.restaurant_dishes_more_button),
                    onClick = onDishNextClick
                )
                Spacer(Modifier.width(8.dp))
                KhContainedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.restaurant_dishes_submit_button),
                    onClick = onSubmitClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RestaurantDishesPreview() {
    KhinkalyatorTheme {
        RestaurantDishesUi(PreviewRestaurantDishesComponent())
    }
}

private class PreviewRestaurantDishesComponent : RestaurantDishesComponent {
    override val dishesComponent = PreviewDishesComponent()

    override val topTitle = LocalizedString.raw("Меню")
    override val bottomTitle = LocalizedString.raw("Добавить позицию в меню")

    override val nameInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )
    override val priceInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )

    override val isDeleteActionVisible = false

    override fun onDishNextClick() = Unit
    override fun onDishDeleteClick() = Unit
    override fun onSubmitClick() = Unit
}