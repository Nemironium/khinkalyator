package me.nemiron.khinkalyator.features.meets.create.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.IconWithBackground
import me.nemiron.khinkalyator.core.widgets.ImageWithText
import me.nemiron.khinkalyator.core.widgets.KhChip
import me.nemiron.khinkalyator.core.widgets.KhToolbar
import me.nemiron.khinkalyator.core.widgets.sheet.ModalBottomSheet
import me.nemiron.khinkalyator.features.meets.create.widgets.KhinkaliButton
import me.nemiron.khinkalyator.features.meets.create.widgets.MediumPersonItem
import me.nemiron.khinkalyator.features.people.person.ui.PersonUi
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId

@OptIn(ExperimentalComposeUiApi::class)
@Composable
// FIXME: Screen doesn't properly adapt Composables sizes after screenSize config changes
fun CreateMeetUi(
    component: CreateMeetComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KhToolbar(stringResource(R.string.create_meet_title))
        },
        floatingActionButton = {
            // FIXME: wrong KhinkaliButton resizing if navigationBarsPadding() used
            KhinkaliButton(
                modifier = Modifier.navigationBarsPadding(),
                state = component.buttonState,
                onClick = component::onCreateMeetClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .navigationBarsPadding()
            ) {
                Spacer(Modifier.height(32.dp))
                RestaurantsContent(
                    restaurants = component.restaurantsViewData,
                    onRestaurantAddClick = component::onRestaurantAddClick,
                    onRestaurantClick = component::onRestaurantClick
                )
                Spacer(Modifier.height(32.dp))
                PeopleContent(
                    people = component.peopleViewData,
                    onPersonAddClick = component::onPersonAddClick,
                    onPersonClick = component::onPersonClick
                )
            }
        }
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheet(
        modifier = modifier
            .imePadding(),
        data = component.personComponent,
        onDismiss = {
            component.onPersonDismissed()
            keyboardController?.hide()
        }
    ) {
        PersonUi(it)
    }
}

@Composable
private fun ColumnScope.RestaurantsContent(
    restaurants: List<RestaurantSimpleViewData>,
    onRestaurantAddClick: () -> Unit,
    onRestaurantClick: (id: RestaurantId) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.create_meet_restaurants_header),
        style = MaterialTheme.appTypography.head2
    )
    Spacer(Modifier.height(24.dp))
    val addRestaurantButtonModifier = if (restaurants.isEmpty()) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
    }
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            KhChip(
                modifier = addRestaurantButtonModifier,
                painter = painterResource(R.drawable.ic_plus_32),
                onClick = onRestaurantAddClick
            )
        }
        items(items = restaurants, key = { it.id }) { restaurant ->
            KhChip(
                text = restaurant.title.resolve(),
                isSelected = restaurant.isSelected,
                onClick = { onRestaurantClick(restaurant.id) }
            )
        }
    }
}

@Composable
private fun ColumnScope.PeopleContent(
    people: List<PersonSimpleViewData>,
    onPersonAddClick: () -> Unit,
    onPersonClick: (id: RestaurantId) -> Unit
) {
    val gridItemWidth = 84.dp
    val gridItemHeight = 110.dp

    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.create_meet_people_header),
        style = MaterialTheme.appTypography.head2
    )
    Spacer(Modifier.height(16.dp))
    LazyVerticalGrid(
        columns = GridCells.Adaptive(gridItemWidth),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AddPersonItem(
                modifier = Modifier
                    .size(gridItemWidth, gridItemHeight)
                    .padding(top = 6.dp),
                onClick = onPersonAddClick
            )
        }

        items(items = people, key = { it.id }) { person ->
            MediumPersonItem(
                modifier = Modifier.size(gridItemWidth, gridItemHeight),
                data = person,
                onClick = { onPersonClick(person.id) }
            )
        }
    }
}

@Composable
private fun AddPersonItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ImageWithText(
        modifier = modifier,
        textPadding = PaddingValues(start = 4.dp, end = 4.dp, top = 14.dp),
        title = stringResource(R.string.create_meet_people_add_title),
        onClick = onClick
    ) {
        IconWithBackground(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.ic_plus_32),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 2.dp,
        )
    }
}