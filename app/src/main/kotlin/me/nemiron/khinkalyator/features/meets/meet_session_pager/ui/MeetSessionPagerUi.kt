package me.nemiron.khinkalyator.features.meets.meet_session_pager.ui

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
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.meets.meet_session_pager.widgets.MeetSessionItem
import me.nemiron.khinkalyator.features.people.domain.PersonId

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MeetSessionPagerUi(
    component: MeetSessionPagerComponent,
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
    data: List<MeetSessionPersonViewData>,
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
            MeetSessionItem(person, onAddDishClick, onDishClick)
        }
    }
}

@Composable
private fun DishesList(
    data: List<MeetSessionDishViewData>,
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
            MeetSessionItem(dish, onAddDishClick, onPersonClick)
        }
    }
}

class PreviewMeetSessionPagerComponent : MeetSessionPagerComponent {
    private val meetSession = MeetSession.MOCKS.first()

    override val initialPage = MeetSessionPagerComponent.Page.People

    override val title = LocalizedString.raw(meetSession.restaurant.name)
    override val buttonTitle = LocalizedString.raw("Счёт ${meetSession.overallSum.formatted()}")
    override val menuData = OverflowMenuData(LocalizedString.raw("Удалить встречу")) { }

    override val peopleViewData = meetSession.toMeetSessionPeopleViewData()
    override val dishesViewData = meetSession.toMeetSessionDishesViewData()

    override fun onCheckButtonClick() = Unit
    override fun onDishPersonClick(dishId: DishId) = Unit
    override fun onDishAddPersonClick(dishId: DishId) = Unit
    override fun onPersonDishClick(personId: PersonId, selectedDishId: DishId) = Unit
    override fun onPersonAddDishClick(personId: PersonId) = Unit
}

private enum class Page : TabRowPage {
    People {
        override val stringRes = R.string.meet_session_pager_people_tab
    },
    Dishes {
        override val stringRes = R.string.meet_session_pager_dishes_tab
    }
}

private fun MeetSessionPagerComponent.Page.toUiPage(): Page {
    return when (this) {
        MeetSessionPagerComponent.Page.People -> Page.People
        MeetSessionPagerComponent.Page.Dishes -> Page.Dishes
    }
}
@Preview(showBackground = true)
@Composable
private fun MeetSessionPagerPreview() {
    KhinkalyatorTheme {
        MeetSessionPagerUi(PreviewMeetSessionPagerComponent())
    }
}