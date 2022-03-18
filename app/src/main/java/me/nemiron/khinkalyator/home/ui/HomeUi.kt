package me.nemiron.khinkalyator.home.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.Children
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
import me.nemiron.khinkalyator.core.ui.utils.createFakeRouterState
import me.nemiron.khinkalyator.core.ui.utils.statusBar
import me.nemiron.khinkalyator.meets.ui.MeetsUi
import me.nemiron.khinkalyator.meets.ui.PreviewMeetsComponent
import me.nemiron.khinkalyator.people.ui.PeopleUi
import me.nemiron.khinkalyator.restaraunts.ui.RestaurantsUi

// FIXME: Save HorizontalPager content across all pages instead of dynamically creating Child
@ExperimentalPagerApi
@Composable
fun HomeUi(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val currentPageIndex = pagerState.currentPage
    val pages = HomeComponent.Page.values().asList()

    LaunchedEffect(currentPageIndex) {
        val newPage = pages[currentPageIndex]
        component.onPageSelected(newPage)
    }

    Scaffold(
        modifier = modifier.statusBar(
            color = MaterialTheme.colors.primary,
            darkIcons = true
        ),
        topBar = {
            if (component.isTabsVisible) {
                TabRow(pagerState, pages, component::onPageSelected)
            }
        },
        content = { paddingValues ->
            HorizontalPager(
                modifier = Modifier.padding(paddingValues),
                count = pages.size,
                state = pagerState
            ) {
                Children(component.routerState) { child ->
                    when (val instance = child.instance) {
                        is HomeComponent.Child.Meets -> MeetsUi(instance.component)
                        is HomeComponent.Child.People -> PeopleUi(instance.component)
                        is HomeComponent.Child.Restaurants -> RestaurantsUi(instance.component)
                    }
                }
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
private fun TabRow(
    pagerState: PagerState,
    pages: List<HomeComponent.Page>,
    onPageSelected: (HomeComponent.Page) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 5.dp
            )
        }
    ) {
        pages.forEachIndexed { index, page ->
            PageTab(
                tabRes = page.stringRes,
                isSelected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                        onPageSelected(page)
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

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
private fun HomeUiPreview() {
    KhinkalyatorTheme {
        HomeUi(PreviewHomeComponent())
    }
}

class PreviewHomeComponent : HomeComponent {
    override val routerState = createFakeRouterState(
        HomeComponent.Child.Meets(PreviewMeetsComponent())
    )

    override val isTabsVisible: Boolean = true

    override fun onPageSelected(page: HomeComponent.Page) = Unit
}