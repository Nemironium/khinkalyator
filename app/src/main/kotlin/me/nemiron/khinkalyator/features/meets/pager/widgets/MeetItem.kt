package me.nemiron.khinkalyator.features.meets.pager.widgets

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.IconWithBackground
import me.nemiron.khinkalyator.core.widgets.KhChip
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.widgets.BigInitialsWithBackground
import me.nemiron.khinkalyator.features.initials.widgets.InitialsBlock
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.meets.pager.ui.OrderViewData
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetDishViewData
import me.nemiron.khinkalyator.features.people.ui.PersonViewData
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetPersonViewData

@Composable
fun MeetItem(
    data: MeetPersonViewData,
    onAddDishClick: (personId: PersonId) -> Unit,
    onDishClick: (personId: PersonId, dishId: DishId) -> Unit,
    modifier: Modifier = Modifier
) {
    MeetItemImpl(
        modifier = modifier,
        title = data.name.resolve(),
        titleWeight = 0.65f,
        price = data.overallSum.resolve(),
        priceWeight = 0.35f,
        image = { BigInitialsWithBackground(data.initials) },
        items = {
            DishesForPerson(
                dishes = data.dishes,
                onAddDishClick = { onAddDishClick(data.personId) },
                onDishClick = { dishId -> onDishClick(data.personId, dishId) }
            )
        }
    )
}

@Composable
fun MeetItem(
    data: MeetDishViewData,
    onAddDishClick: (dishId: DishId) -> Unit,
    onPersonClick: (dishId: DishId) -> Unit,
    modifier: Modifier = Modifier
) {
    MeetItemImpl(
        modifier = modifier,
        title = data.name.resolve(),
        titleWeight = 0.75f,
        price = data.price.resolve(),
        priceWeight = 0.25f,
        image = {
            // TODO: ask Sveta about how to determine food icons
            IconWithBackground(
                painter = painterResource(R.drawable.ic_kginkali_colored_40),
                shouldBeColored = false,
                backgroundColor = Color(0xFFEAF1F8)
            )
        },
        items = {
            PeopleForDish(
                people = data.people,
                onAddDishClick = { onAddDishClick(data.dishId) },
                onPersonClick = { onPersonClick(data.dishId) }
            )
        }
    )
}

@Composable
private fun ColumnScope.DishesForPerson(
    dishes: List<OrderViewData>,
    onAddDishClick: () -> Unit,
    onDishClick: (dishId: DishId) -> Unit
) {
    if (dishes.isEmpty()) {
        AddButtonBig(onAddDishClick)
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                AddButtonSmall(onAddDishClick)
            }
            items(items = dishes, key = { it.dishId }) { dish ->
                val onDishClickLambda = { onDishClick(dish.dishId) }
                val title = dish.title.resolve()

                if (dish.sharedInitials != null) {
                    KhChip(
                        text = title,
                        onClick = onDishClickLambda,
                        icon = {
                            InitialsBlock(dish.sharedInitials, visibleCount = 2)
                        }
                    )
                } else {
                    KhChip(text = title, onClick = onDishClickLambda)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.PeopleForDish(
    people: List<PersonViewData>,
    onAddDishClick: () -> Unit,
    onPersonClick: () -> Unit
) {
    if (people.isEmpty()) {
        AddButtonBig(onAddDishClick)
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item { AddButtonSmall(onAddDishClick) }
            items(items = people, key = { it.personId }) { person ->
                KhChip(
                    text = person.title.resolve(),
                    onClick = onPersonClick
                )
            }
        }
    }
}

@Composable
private fun AddButtonBig(onClick: () -> Unit, modifier: Modifier = Modifier) {
    KhChip(
        modifier = modifier
            .padding(start = 56.dp, end = 16.dp)
            .fillMaxWidth(),
        painter = painterResource(R.drawable.ic_plus_32),
        onClick = onClick
    )
}

@Composable
private fun AddButtonSmall(onClick: () -> Unit, modifier: Modifier = Modifier) {
    KhChip(
        modifier = modifier.size(40.dp),
        painter = painterResource(R.drawable.ic_plus_32),
        onClick = onClick
    )
}

@Composable
private fun MeetItemImpl(
    title: String,
    price: String,
    @FloatRange(from = .0, to = 1.0) titleWeight: Float,
    @FloatRange(from = .0, to = 1.0) priceWeight: Float,
    image: @Composable RowScope.() -> Unit = {},
    items: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(start = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            image()
            Spacer(Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(titleWeight),
                text = title,
                style = MaterialTheme.appTypography.medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(priceWeight),
                text = price,
                textAlign = TextAlign.End,
                color = MaterialTheme.additionalColors.secondaryOnPrimary,
                style = MaterialTheme.appTypography.medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.height(8.dp))
        items()
    }
}

@Preview
@Composable
private fun MeetItemPreview() {
    val initials = listOf(
        InitialsViewData("КЕ", Emoji("🐨")),
        InitialsViewData("КВ", Emoji("🦄")),
        InitialsViewData("ЭЗ", Emoji("🐼"))
    )

    KhinkalyatorTheme {
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            MeetItem(
                data = MeetPersonViewData(
                    personId = 1,
                    initials = initials.last(),
                    name = LocalizedString.raw("Элина Зайникеева"),
                    overallSum = LocalizedString.raw("100 000 ₽"),
                    dishes = emptyList()
                ),
                onDishClick = { _, _ -> },
                onAddDishClick = { }
            )

            MeetItem(
                data = MeetPersonViewData(
                    personId = 1,
                    initials = initials.first(),
                    name = LocalizedString.raw("Кауров Евгений"),
                    overallSum = LocalizedString.raw("630 ₽"),
                    dishes = listOf(
                        OrderViewData(
                            dishId = 1,
                            title = LocalizedString.raw("Хачапури по-имеритински 1"),
                            sharedInitials = initials.subList(1, 3)
                        ),
                        OrderViewData(
                            dishId = 2,
                            title = LocalizedString.raw("Хинкали 6"),
                            sharedInitials = null
                        )
                    )
                ),
                onDishClick = { _, _ -> },
                onAddDishClick = { }
            )

            MeetItem(
                data = MeetDishViewData(
                    dishId = 1,
                    name = LocalizedString.raw("Капучино"),
                    price = LocalizedString.raw("200 ₽"),
                    people = emptyList()
                ),
                onPersonClick = { },
                onAddDishClick = { }
            )

            MeetItem(
                data = MeetDishViewData(
                    dishId = 1,
                    name = LocalizedString.raw("Хачапури по-имеритински"),
                    price = LocalizedString.raw("4500 ₽"),
                    people = listOf(
                        PersonViewData(1, LocalizedString.raw("Кауров Евгений"), false),
                        PersonViewData(2, LocalizedString.raw("Элина Зайникеева"), false),
                        PersonViewData(3, LocalizedString.raw("Корешин Владимир"), false),
                    )
                ),
                onPersonClick = { },
                onAddDishClick = { }
            )
        }
    }
}