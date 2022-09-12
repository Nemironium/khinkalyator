package me.nemiron.khinkalyator.features.home.ui

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.utils.statusBar
import me.nemiron.khinkalyator.core.widgets.KhTabRow
import me.nemiron.khinkalyator.core.widgets.TabRowPage
import me.nemiron.khinkalyator.core.widgets.sheet.ModalBottomSheet
import me.nemiron.khinkalyator.features.meets.home_page.ui.MeetsPageUi
import me.nemiron.khinkalyator.features.meets.home_page.ui.PreviewMeetsPageComponent
import me.nemiron.khinkalyator.features.people.home_page.ui.PeoplePageUi
import me.nemiron.khinkalyator.features.people.home_page.ui.PreviewPeoplePageComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonUi
import me.nemiron.khinkalyator.features.people.person.ui.PreviewPersonComponent
import me.nemiron.khinkalyator.features.restaraunts.home_page.ui.PreviewRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.home_page.ui.RestaurantsPageUi

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeUi(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val pages = remember { Page.values().asList() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        component.closeKeyboardEvents.collectLatest {
            keyboardController?.hide()
        }
    }

    Scaffold(
        modifier = modifier.statusBar(MaterialTheme.colors.primary),
        topBar = {
            KhTabRow(pagerState, pages)
        },
        content = { contentPadding ->
            HorizontalPager(
                modifier = Modifier.padding(contentPadding),
                count = pages.size,
                state = pagerState
            ) { page ->
                when (pages[page]) {
                    Page.Meets -> MeetsPageUi(component.meetsPageComponent)
                    Page.Restaurants -> RestaurantsPageUi(component.restaurantsPageComponent)
                    Page.People -> PeoplePageUi(component.peoplePageComponent)
                }
            }
        }
    )

    ModalBottomSheet(
        modifier = modifier.imePadding(),
        data = component.personComponent,
        onDismiss = component::onPersonDismissed
    ) {
        PersonUi(it)
    }
}

private enum class Page : TabRowPage {
    Meets {
        override val stringRes = R.string.home_meets_tab
    },
    Restaurants {
        override val stringRes = R.string.home_restaurants_tab
    },
    People {
        override val stringRes = R.string.home_people_tab
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    KhinkalyatorTheme {
        HomeUi(PreviewHomeComponent())
    }
}

class PreviewHomeComponent : HomeComponent {
    override val meetsPageComponent = PreviewMeetsPageComponent()
    override val restaurantsPageComponent = PreviewRestaurantsPageComponent()
    override val peoplePageComponent = PreviewPeoplePageComponent()
    override val personComponent = PreviewPersonComponent()
    override val closeKeyboardEvents: Flow<Unit> = flow { }
    override fun onPersonDismissed() = Unit
}