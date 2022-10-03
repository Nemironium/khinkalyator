package me.nemiron.khinkalyator.features.meets.home_page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.catch
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetsUseCase
import timber.log.Timber

class RealMeetsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (MeetsPageComponent.Output) -> Unit,
    observeMeets: ObserveMeetsUseCase
) : MeetsPageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val meetsState by observeMeets().catch {
        Timber.tag("MEET_TAG").d("RealMeetsPageComponent.observeMeets catch: ")
    }.toComposeState(
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