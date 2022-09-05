package me.nemiron.khinkalyator.features.people.page.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.page.widgets.BigPersonItem
import timber.log.Timber

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
                text = stringResource(R.string.people_add_button),
                onClick = component::onPersonAddClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            PeopleList(
                peopleViewData = component.peopleViewData,
                onPersonClick = component::onPersonClick,
                onPersonDeleteClick = component::onPersonDeleteClick,
                fabButtonPadding = 100.dp
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun PeopleList(
    peopleViewData: List<PersonFullViewData>,
    onPersonClick: (personId: PersonId) -> Unit,
    onPersonDeleteClick: (personId: PersonId) -> Unit,
    fabButtonPadding: Dp,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp, bottom = fabButtonPadding)
    ) {
        items(items = peopleViewData, key = { it.id }) { person ->
            val dismissThresholdValue = 0.25f
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onPersonDeleteClick(person.id)
                        true
                    } else {
                        false
                    }
                })

            // FIXME: SwipeToDismiss blocks end-to-start gesture to return to RestaurantsPageUi
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { FractionalThreshold(dismissThresholdValue) },
                background = {
                    if (dismissState.dismissDirection != DismissDirection.EndToStart) return@SwipeToDismiss

                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> MaterialTheme.additionalColors.secondaryContainer
                            DismissValue.DismissedToEnd, DismissValue.DismissedToStart -> MaterialTheme.colors.secondary
                        }
                    )

                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 1f else 1.5f
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            modifier = Modifier.scale(scale),
                            painter = painterResource(R.drawable.ic_delete_32),
                            contentDescription = null,
                            tint = MaterialTheme.additionalColors.onSurfaceContainer
                        )
                    }
                }, dismissContent = {
                    BigPersonItem(
                        modifier = Modifier.animateItemPlacement(),
                        data = person,
                        onClick = { onPersonClick((person.id)) }
                    )
                }
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