package me.nemiron.khinkalyator.features.meets.pager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.widgets.KhTabRow
import me.nemiron.khinkalyator.core.widgets.KhToolbar
import me.nemiron.khinkalyator.core.widgets.OverflowMenu
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.core.widgets.TabRowPage
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.features.meets.pager.widgets.MeetItem
import me.nemiron.khinkalyator.common_domain.model.PersonId

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MeetPagerUi(
    component: MeetPagerComponent,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    with(rememberCoroutineScope()) {
        // prevent extra scrolling when returning from other component
        var isAlreadyScrolled by rememberSaveable { mutableStateOf(false) }
        val initialPage = component.initialPage.toUiPage()

        launch {
            if (!isAlreadyScrolled) {
                pagerState.scrollToPage(initialPage.ordinal)
                isAlreadyScrolled = true
            }
        }
    }

    val pages = remember { Page.values().asList() }

    Scaffold(
        modifier = modifier,
        topBar = {
            KhToolbar(
                title = component.title.resolve(),
                actionIcon = { OverflowMenu(component.menuData) }
            )
        },
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier.navigationBarsPadding(),
                text = component.buttonTitle.resolve(),
                onClick = component::onCheckButtonClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { contentPadding ->
            val fabButtonPadding = 100.dp
            Column(Modifier.padding(contentPadding)) {
                KhTabRow(pagerState, pages)
                HorizontalPager(
                    count = pages.size,
                    state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    when (pages[page]) {
                        Page.People -> PeopleList(
                            data = component.peopleViewData,
                            onAddDishClick = component::onPersonAddDishClick,
                            onDishClick = component::onPersonDishClick,
                            fabButtonPadding = fabButtonPadding
                        )
                        Page.Dishes -> DishesList(
                            data = component.dishesViewData,
                            onAddDishClick = component::onDishAddPersonClick,
                            onPersonClick = component::onDishPersonClick,
                            fabButtonPadding = fabButtonPadding
                        )
                    }
                }
            }

        }
    )
}

@Composable
private fun PeopleList(
    data: List<MeetPersonViewData>,
    onAddDishClick: (personId: PersonId) -> Unit,
    onDishClick: (personId: PersonId, dishId: DishId) -> Unit,
    fabButtonPadding: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 24.dp, bottom = fabButtonPadding),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(items = data, key = { it.personId }) { person ->
            MeetItem(person, onAddDishClick, onDishClick)
        }
    }
}

@Composable
private fun DishesList(
    data: List<MeetDishViewData>,
    onAddDishClick: (dishId: DishId) -> Unit,
    onPersonClick: (dishId: DishId) -> Unit,
    fabButtonPadding: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 24.dp, bottom = fabButtonPadding),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(items = data, key = { it.dishId }) { dish ->
            MeetItem(dish, onAddDishClick, onPersonClick)
        }
    }
}

class PreviewMeetPagerComponent : MeetPagerComponent {
    private val meet = Meet.MOCKS.first()

    override val initialPage = MeetPagerComponent.Page.People

    override val title = LocalizedString.raw(meet.restaurant.name)
    override val buttonTitle = LocalizedString.raw("Счёт ${meet.overallSum.formatted()}")
    override val menuData = OverflowMenuData(LocalizedString.raw("Удалить встречу")) { }

    override val peopleViewData = meet.toMeetPeopleViewData()
    override val dishesViewData = meet.toMeetDishesViewData()

    override fun onCheckButtonClick() = Unit
    override fun onDishPersonClick(dishId: DishId) = Unit
    override fun onDishAddPersonClick(dishId: DishId) = Unit
    override fun onPersonDishClick(personId: PersonId, selectedDishId: DishId) = Unit
    override fun onPersonAddDishClick(personId: PersonId) = Unit
}

private enum class Page : TabRowPage {
    People {
        override val stringRes = R.string.meet_pager_people_tab
    },
    Dishes {
        override val stringRes = R.string.meet_pager_dishes_tab
    }
}

private fun MeetPagerComponent.Page.toUiPage(): Page {
    return when (this) {
        MeetPagerComponent.Page.People -> Page.People
        MeetPagerComponent.Page.Dishes -> Page.Dishes
    }
}
@Preview(showBackground = true)
@Composable
private fun MeetPagerPreview() {
    KhinkalyatorTheme {
        MeetPagerUi(PreviewMeetPagerComponent())
    }
}