package me.nemiron.khinkalyator.core.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography

interface TabRowPage {
    val stringRes: Int
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun KhTabRow(
    state: PagerState,
    pages: List<TabRowPage>,
    modifier: Modifier = Modifier,
    indicatorHeight: Dp = 5.dp
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        modifier = modifier.height(56.dp),
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