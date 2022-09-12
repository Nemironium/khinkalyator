package me.nemiron.khinkalyator.features.meets.meet_session_pager.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetSessionUseCase
import me.nemiron.khinkalyator.features.people.domain.PersonId

class RealMeetSessionPagerComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    override val initialPage: MeetSessionPagerComponent.Page,
    private val onOutput: (MeetSessionPagerComponent.Output) -> Unit,
    observeMeetSessionUseCase: ObserveMeetSessionUseCase
) : MeetSessionPagerComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val meetSessionState by observeMeetSessionUseCase(meetId).toComposeState(
        initialValue = null,
        coroutineScope = coroutineScope
    )

    override val title by derivedStateOf {
        LocalizedString.raw(meetSessionState?.restaurant?.name.orEmpty())
    }
    override val buttonTitle by derivedStateOf {
        val overallSum = meetSessionState?.overallSum?.formatted() ?: "0"
        LocalizedString.resource(R.string.meet_session_pager_overall_sum_count, overallSum)
    }

    override val menuData = OverflowMenuData(
        title = LocalizedString.resource(R.string.meet_session_pager_delete_menu_action),
        onMenuItemClick = ::onDeleteClick
    )
    override val peopleViewData by derivedStateOf {
        meetSessionState?.toMeetSessionPeopleViewData() ?: emptyList()
    }

    override val dishesViewData by derivedStateOf {
        meetSessionState?.toMeetSessionDishesViewData() ?: emptyList()
    }

    override fun onCheckButtonClick() {
        meetSessionState?.overallSum?.let {
            if (it.value > 0) {
                onOutput(MeetSessionPagerComponent.Output.CheckRequested)
            }
        }
    }

    override fun onDishPersonClick(dishId: DishId) =
        onOutput(MeetSessionPagerComponent.Output.DishDetailsRequested(dishId))

    override fun onDishAddPersonClick(dishId: DishId) =
        onOutput(MeetSessionPagerComponent.Output.DishDetailsRequested(dishId))

    override fun onPersonDishClick(personId: PersonId, selectedDishId: DishId) =
        onOutput(MeetSessionPagerComponent.Output.PersonDetailsRequested(personId, selectedDishId))

    override fun onPersonAddDishClick(personId: PersonId) =
        onOutput(MeetSessionPagerComponent.Output.PersonDetailsRequested(personId, null))

    private fun onDeleteClick() = onOutput(MeetSessionPagerComponent.Output.DeleteRequested)
}