package me.nemiron.khinkalyator.features.home.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.theme.additionalColors
import me.nemiron.khinkalyator.core.ui.theme.appTypography
import me.nemiron.khinkalyator.core.ui.utils.statusBar
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsUi
import me.nemiron.khinkalyator.features.meets.page.ui.PreviewMeetsPageComponent
import me.nemiron.khinkalyator.features.people.page.ui.PeoplePageUi
import me.nemiron.khinkalyator.features.people.page.ui.PreviewPeoplePageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.PreviewRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageUi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeUi(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val pages = remember { HomeComponent.Page.values().asList() }

    Scaffold(
        modifier = modifier.statusBar(MaterialTheme.colors.primary),
        topBar = {
            PagerTabRow(pagerState, pages)
        },
        content = { contentPadding ->
            HorizontalPager(
                modifier = Modifier.padding(contentPadding),
                count = pages.size,
                state = pagerState
            ) { page ->
                when (pages[page]) {
                    HomeComponent.Page.Meets -> MeetsUi(component.meetsPageComponent)
                    HomeComponent.Page.Restaurants -> RestaurantsPageUi(component.restaurantsPageComponent)
                    HomeComponent.Page.People -> PeoplePageUi(component.peoplePageComponent)
                }
            }
        }
    )
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PagerTabRow(
    state: PagerState,
    pages: List<HomeComponent.Page>,
    indicatorHeight: Dp = 5.dp
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        modifier = Modifier.height(56.dp),
        selectedTabIndex = state.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(state, tabPositions),
                height = indicatorHeight
            )
        }
    ) {
        pages.forEachIndexed { index, page ->
            PageTab(
                tabRes = page.stringRes,
                isSelected = state.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@Composable
private fun PageTab(
    @StringRes tabRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        text = {
            Text(
                text = stringResource(tabRes),
                style = MaterialTheme.appTypography.head3
            )
        },
        selected = isSelected,
        onClick = onClick,
        selectedContentColor = MaterialTheme.colors.onPrimary,
        unselectedContentColor = MaterialTheme.additionalColors.secondaryOnPrimary
    )
}

@get:StringRes
private val HomeComponent.Page.stringRes: Int
    get() = when (this) {
        HomeComponent.Page.Meets -> R.string.home_meets_tab
        HomeComponent.Page.Restaurants -> R.string.home_restaurants_tab
        HomeComponent.Page.People -> R.string.home_people_tab
    }

@Preview(showBackground = true)
@Composable
private fun HomeUiPreview() {
    KhinkalyatorTheme {
        HomeUi(PreviewHomeComponent())
    }
}

class PreviewHomeComponent : HomeComponent {
    override val meetsPageComponent = PreviewMeetsPageComponent()
    override val restaurantsPageComponent = PreviewRestaurantsPageComponent()
    override val peoplePageComponent = PreviewPeoplePageComponent()
}