package me.nemiron.khinkalyator.features.restaraunts.menu.ui

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
import androidx.compose.material.ProvideTextStyle
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
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
import me.nemiron.khinkalyator.core.utils.statusBar
import me.nemiron.khinkalyator.core.widgets.*
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.DishId
import kotlin.random.Random

@Composable
fun MenuDetailsUi(
    component: MenuDetailsComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.dismissOnTapOutsideElements(),
        topBar = {
            ProvideTextStyle(value = TextStyle(color = MaterialTheme.colors.onSecondary)) {
                KhToolbar(
                    title = component.topTitle.resolve(),
                    navigationIcon = {
                        IconWithBackground(
                            modifier = Modifier.padding(start = 16.dp),
                            painter = painterResource(R.drawable.ic_restaurant_32),
                            contentColor = MaterialTheme.colors.onSurface,
                            backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
                        )
                    },
                    backgroundColor = MaterialTheme.additionalColors.secondaryBackground,
                    statusBarModifier = Modifier.statusBar(
                        color = MaterialTheme.additionalColors.secondaryBackground,
                        darkIcons = true
                    )
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.additionalColors.secondaryBackground)
            ) {
                val modalPadding = 248.dp
                DishesContent(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = modalPadding)
                        .verticalScroll(rememberScrollState()),
                    dishes = component.dishesViewData,
                    onDishAddClick = component::onDishAddClick,
                    onDishClick = component::onDishClick
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

@Composable
private fun DishesContent(
    dishes: List<DishViewData>,
    onDishAddClick: () -> Unit,
    onDishClick: (id: DishId) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(R.string.menu_details_title),
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
            if (dishes.isNotEmpty()) {
                KhChip(
                    painter = painterResource(R.drawable.ic_plus_32),
                    onClick = onDishAddClick
                )
            }
            dishes.forEach { dish ->
                KhChip(
                    text = dish.name.resolve(),
                    isSelected = dish.isSelected,
                    onClick = { onDishClick(dish.id) }
                )
            }
        }
    }
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
                        contentColor = MaterialTheme.colors.onSurface,
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
                    placeholder = stringResource(R.string.menu_details_name_placeholder),
                    leadingIcon = {
                        IconWithBackground(
                            painter = painterResource(R.drawable.ic_food_32),
                            contentColor = MaterialTheme.colors.onSurface,
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
                    placeholder = stringResource(R.string.menu_details_price_placeholder),
                    trailingIcon = {
                        Text(
                            text = stringResource(R.string.menu_details_price_postfix),
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
                    text = stringResource(R.string.menu_details_more_button),
                    onClick = onDishNextClick
                )
                Spacer(Modifier.width(8.dp))
                KhContainedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.menu_details_submit_button),
                    onClick = onSubmitClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuDetailsPreview() {
    KhinkalyatorTheme {
        MenuDetailsUi(PreviewMenuDetailsComponent())
    }
}

private class PreviewMenuDetailsComponent : MenuDetailsComponent {
    override val dishesViewData: List<DishViewData> = listOf(
        Dish(
            id = Random.nextLong(),
            name = "Хинкали с грибами и сыром",
            price = 95.0
        ),
        Dish(
            id = Random.nextLong(),
            name = "Хинкали с бараниной",
            price = 90.0
        ),
        Dish(
            id = Random.nextLong(),
            name = "Хачапури по-аджарски S",
            price = 450.0
        ),
        Dish(
            id = Random.nextLong(),
            name = "Чай в чайнике",
            price = 250.0
        )
    ).map(Dish::toDishViewData)

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

    override fun onDishAddClick() = Unit

    override fun onDishNextClick() = Unit

    override fun onDishDeleteClick() = Unit

    override fun onSubmitClick() = Unit

    override fun onDishClick(dishId: DishId) = Unit
}