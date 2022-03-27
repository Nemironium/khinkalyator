package me.nemiron.khinkalyator.features.people.page.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import me.aartikov.sesame.dialog.DialogControl
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.ui.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.ui.widgets.sheet.ModalBottomSheet
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.page.widgets.BigPersonItem
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonUi
import me.nemiron.khinkalyator.features.people.person.ui.PreviewPersonComponent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PeoplePageUi(
    component: PeoplePageComponent,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            KhContainedButton(
                modifier = Modifier
                    .navigationBarsPadding(),
                text = stringResource(R.string.people_add_button),
                onClick = component::onPersonAddClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            LaunchedEffect(Unit) {
                component.closeKeyboardEvents.collectLatest {
                    keyboardController?.hide()
                }
            }

            PeopleList(component, fabButtonPadding = 100.dp)
        }
    )
    PersonBottomSheet(
        modifier = modifier
            .imePadding(),
        component = component.personComponent,
        dialogControl = component.personDialogControl,
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

@Composable
private fun PersonBottomSheet(
    component: PersonComponent,
    dialogControl: DialogControl<Unit, Unit>,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier,
        dialogControl = dialogControl,
        sheetContent = {
            PersonUi(
                component = component
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PeoplePageUiPreview() {
    KhinkalyatorTheme {
        PeoplePageUi(PreviewPeoplePageComponent())
    }
}

class PreviewPeoplePageComponent : PeoplePageComponent {
    override val personComponent = PreviewPersonComponent()
    override val personDialogControl = DialogControl<Unit, Unit>()
    override val closeKeyboardEvents: Flow<Unit> = flow { }
    override val peopleViewData: List<PersonFullViewData> = emptyList()

    override fun onPersonAddClick() = Unit
    override fun onPersonDeleteClick(personId: PersonId) = Unit
    override fun onPersonClick(personId: PersonId) = Unit
}