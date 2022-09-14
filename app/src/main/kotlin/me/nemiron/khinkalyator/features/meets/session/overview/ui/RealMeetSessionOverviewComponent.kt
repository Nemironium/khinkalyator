package me.nemiron.khinkalyator.features.meets.session.overview.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.ComponentHolder
import me.nemiron.khinkalyator.core.componentHolder
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.log
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.meets.createMeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.createMeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.createMeetSessionPagerComponent
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.MeetSession
import me.nemiron.khinkalyator.features.meets.session.check.ui.MeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.session.details.ui.MeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.session.domain.ArchiveMeetSessionUseCase
import me.nemiron.khinkalyator.features.meets.session.domain.DeleteMeetSessionUseCase
import me.nemiron.khinkalyator.features.meets.session.domain.ObserveMeetSessionUseCase
import me.nemiron.khinkalyator.features.meets.session.pager.ui.MeetSessionPagerComponent

class RealMeetSessionOverviewComponent(
    componentContext: ComponentContext,
    private val meetId: MeetId,
    private val onOutput: (MeetSessionOverviewComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    observeMeetSession: ObserveMeetSessionUseCase,
    private val deleteMeetSession: DeleteMeetSessionUseCase,
    private val archiveMeetSession: ArchiveMeetSessionUseCase
) : MeetSessionOverviewComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = ChildConfiguration.Pager(
            MeetSessionPagerComponent.Page.People,
            meetId
        ),
        handleBackButton = true,
        childFactory = ::createChild
    ).log("MeetSessionOverview")

    private val checkComponentHolder: ComponentHolder<MeetSessionCheckComponent.Configuration, MeetSessionCheckComponent> =
        componentHolder(
            key = "CheckComponent",
            removeOnBackPressed = true
        ) { configuration, componentContext ->
            componentFactory.createMeetSessionCheckComponent(
                componentContext,
                configuration,
                onOutput = ::onCheckOutput
            )
        }

    private val meetSessionState by observeMeetSession(meetId).toComposeState(
        initialValue = null,
        coroutineScope = coroutineScope
    )

    private val sessionType by derivedStateOf {
        meetSessionState?.type ?: MeetSession.Type.Completed
    }

    override val childStackState: ChildStack<*, MeetSessionOverviewComponent.Child> by stack.toComposeState(
        lifecycle
    )

    override val checkComponent: MeetSessionCheckComponent?
        get() = checkComponentHolder.component

    override fun onCheckDismissed() {
        checkComponentHolder.configuration = null
    }

    private fun createChild(
        config: ChildConfiguration,
        componentContext: ComponentContext
    ): MeetSessionOverviewComponent.Child =
        when (config) {
            is ChildConfiguration.Details -> MeetSessionOverviewComponent.Child.Details(
                componentFactory.createMeetSessionDetailsComponent(
                    componentContext,
                    config.detailsConfiguration
                )
            )
            is ChildConfiguration.Pager -> MeetSessionOverviewComponent.Child.Pager(
                componentFactory.createMeetSessionPagerComponent(
                    componentContext,
                    config.meetId,
                    config.initialPage,
                    onOutput = ::onPagerOutput
                )
            )
        }

    private fun onPagerOutput(output: MeetSessionPagerComponent.Output) {
        when (output) {
            is MeetSessionPagerComponent.Output.CheckRequested -> {
                val configuration = when (sessionType) {
                    MeetSession.Type.Active -> MeetSessionCheckComponent.Configuration.ActiveSession(
                        meetId
                    )
                    MeetSession.Type.Completed -> MeetSessionCheckComponent.Configuration.ArchivedSession(
                        meetId
                    )
                }
                checkComponentHolder.configuration = configuration
            }
            is MeetSessionPagerComponent.Output.DeleteRequested -> {
                coroutineScope.launch {
                    deleteMeetSession(meetId)
                    onOutput(MeetSessionOverviewComponent.Output.MeetSessionCloseRequested)
                }
            }
            is MeetSessionPagerComponent.Output.DishDetailsRequested -> {
                if (sessionType == MeetSession.Type.Active) {
                    navigation.push(
                        ChildConfiguration.Details(
                            MeetSessionDetailsComponent.Configuration.DishDetails(
                                meetId = meetId,
                                dishId = output.dishId
                            )
                        )
                    )
                } else {
                    // TODO: show error message like snack
                }
            }
            is MeetSessionPagerComponent.Output.PersonDetailsRequested -> {
                if (sessionType == MeetSession.Type.Active) {
                    navigation.push(
                        ChildConfiguration.Details(
                            MeetSessionDetailsComponent.Configuration.PersonDetails(
                                meetId = meetId,
                                personId = output.personId,
                                selectedDishId = output.selectedDishId
                            )
                        )
                    )
                } else {
                    // TODO: show error message like snack
                }
            }
        }
    }

    private fun onCheckOutput(output: MeetSessionCheckComponent.Output) {
        when (output) {
            MeetSessionCheckComponent.Output.SessionEndRequested -> {
                coroutineScope.launch {
                    checkComponentHolder.configuration = null
                    archiveMeetSession(meetId)
                    onOutput(MeetSessionOverviewComponent.Output.MeetSessionCloseRequested)
                }
            }
        }
    }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        class Pager(
            val initialPage: MeetSessionPagerComponent.Page,
            val meetId: MeetId
        ) : ChildConfiguration

        @Parcelize
        class Details(
            val detailsConfiguration: MeetSessionDetailsComponent.Configuration
        ) : ChildConfiguration
    }
}