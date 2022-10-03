package me.nemiron.khinkalyator.features.meets.pager.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.features.dishes.ui.formatted
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetUseCase
import me.nemiron.khinkalyator.common_domain.model.PersonId

class RealMeetPagerComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    override val initialPage: MeetPagerComponent.Page,
    private val onOutput: (MeetPagerComponent.Output) -> Unit,
    observeMeet: ObserveMeetUseCase
) : MeetPagerComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val meetState by observeMeet(meetId).toComposeState(
        initialValue = null,
        coroutineScope = coroutineScope
    )

    override val title by derivedStateOf {
        LocalizedString.raw(meetState?.restaurant?.name.orEmpty())
    }
    override val buttonTitle by derivedStateOf {
        val overallSum = meetState?.overallSum?.formatted() ?: "0"
        LocalizedString.resource(R.string.meet_pager_overall_sum_count, overallSum)
    }

    override val menuData = OverflowMenuData(
        title = LocalizedString.resource(R.string.meet_pager_delete_menu_action),
        onMenuItemClick = ::onDeleteClick
    )
    override val peopleViewData by derivedStateOf {
        meetState?.toMeetPeopleViewData() ?: emptyList()
    }

    override val dishesViewData by derivedStateOf {
        meetState?.toMeetDishesViewData() ?: emptyList()
    }

    override fun onCheckButtonClick() {
        meetState?.overallSum?.let {
            if (it.value > 0) {
                onOutput(MeetPagerComponent.Output.CheckRequested)
            }
        }
    }

    override fun onDishPersonClick(dishId: DishId) =
        onOutput(MeetPagerComponent.Output.DishDetailsRequested(dishId))

    override fun onDishAddPersonClick(dishId: DishId) =
        onOutput(MeetPagerComponent.Output.DishDetailsRequested(dishId))

    override fun onPersonDishClick(personId: PersonId, selectedDishId: DishId) =
        onOutput(MeetPagerComponent.Output.PersonDetailsRequested(personId, selectedDishId))

    override fun onPersonAddDishClick(personId: PersonId) =
        onOutput(MeetPagerComponent.Output.PersonDetailsRequested(personId, null))

    private fun onDeleteClick() = onOutput(MeetPagerComponent.Output.DeleteRequested)
}