package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography

interface TabRowPage {
    val stringRes: Int
}

@Composable
fun KhTabRowPlaceholder(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .placeholder(
                visible = isVisible,
                shape = RectangleShape,
                highlight = PlaceholderHighlight.fade(),
            )
    )
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
                title = stringResource(page.stringRes),
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
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Tab(
        modifier = modifier,
        text = {
            Text(
                text = title,
                style = MaterialTheme.appTypography.head3
            )
        },
        selected = isSelected,
        onClick = onClick,
        selectedContentColor = MaterialTheme.colors.onPrimary,
        unselectedContentColor = MaterialTheme.additionalColors.secondaryOnPrimary
    )
}