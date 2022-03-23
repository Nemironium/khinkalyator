package me.nemiron.khinkalyator.people.page.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton
import me.nemiron.khinkalyator.people.domain.PersonId
import me.nemiron.khinkalyator.people.page.widgets.BigPersonItem

@Composable
fun PeoplePageUi(
    component: PeoplePageComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.people_add),
                onClick = component::onPersonAddClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            PeopleList(component, fabButtonPadding = 100.dp)
        }
    )
}

@Composable
private fun PeopleList(
    component: PeoplePageComponent,
    fabButtonPadding: Dp,
    modifier: Modifier = Modifier
) {
    // FIXME: add swipe-to-dismiss effect
    // FIXME: add ItemPlacement animation
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp, bottom = fabButtonPadding)
    ) {
        items(items = component.peopleViewData, key = { it.id }) { person ->
            BigPersonItem(
                data = person,
                onClick = { component.onPersonClick((person.id)) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PeoplePageUiPreview() {
    KhinkalyatorTheme {
        PeoplePageUi(PreviewPeoplePageComponent())
    }
}

class PreviewPeoplePageComponent : PeoplePageComponent {
    override val peopleViewData: List<PersonFullViewData> = emptyList()

    override fun onPersonAddClick() = Unit
    override fun onPersonDeleteClick(personId: PersonId) = Unit
    override fun onPersonClick(personId: PersonId) = Unit
}