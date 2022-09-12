package me.nemiron.khinkalyator.features.meets.home_page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.meets.domain.Meet
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetsUseCase

class RealMeetsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (MeetsPageComponent.Output) -> Unit,
    observeMeets: ObserveMeetsUseCase
) : MeetsPageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val meetsState by observeMeets().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    override val meetsViewData by derivedStateOf {
        meetsState.map(Meet::toMeetHomePageViewData)
    }

    override fun onMeetAddClick() {
        onOutput(MeetsPageComponent.Output.NewMeetRequested)
    }

    override fun onMeetClick(meetId: MeetId) {
        onOutput(MeetsPageComponent.Output.MeetRequested(meetId))
    }
}