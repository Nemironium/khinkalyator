package me.nemiron.khinkalyator.features.meets.meet_session_overview.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.ComponentHolder
import me.nemiron.khinkalyator.core.componentHolder
import me.nemiron.khinkalyator.core.utils.log
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.meets.createMeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.createMeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.createMeetSessionPagerComponent
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.meet_session_check.ui.MeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.meet_session_details.ui.MeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.meet_session_pager.ui.MeetSessionPagerComponent

class RealMeetSessionOverviewComponent(
    componentContext: ComponentContext,
    private val meetId: MeetId,
    private val onOutput: (MeetSessionOverviewComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory
) : MeetSessionOverviewComponent, ComponentContext by componentContext {

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

    private fun onPagerOutput(output: MeetSessionPagerComponent.Output) =
        when (output) {
            is MeetSessionPagerComponent.Output.CheckRequested -> {
                // TODO: check status of current meet session and set correct configuration
                checkComponentHolder.configuration =
                    MeetSessionCheckComponent.Configuration.ActiveSession(meetId)
            }
            is MeetSessionPagerComponent.Output.DeleteRequested -> {
                // TODO: delete current meet  via UseCase
                onOutput(MeetSessionOverviewComponent.Output.MeetSessionCloseRequested)
            }
            is MeetSessionPagerComponent.Output.DishDetailsRequested -> {
                navigation.push(
                    ChildConfiguration.Details(
                        MeetSessionDetailsComponent.Configuration.DishDetails(
                            meetId = meetId,
                            dishId = output.dishId
                        )
                    )
                )
            }
            is MeetSessionPagerComponent.Output.PersonDetailsRequested -> {
                navigation.push(
                    ChildConfiguration.Details(
                        MeetSessionDetailsComponent.Configuration.PersonDetails(
                            meetId = meetId,
                            personId = output.personId,
                            selectedDishId = output.selectedDishId
                        )
                    )
                )
            }
        }

    private fun onCheckOutput(output: MeetSessionCheckComponent.Output) = when (output) {
        MeetSessionCheckComponent.Output.SessionEndRequested -> {
            checkComponentHolder.configuration = null
            // TODO: set current meet as archived via UseCase
            onOutput(MeetSessionOverviewComponent.Output.MeetSessionCloseRequested)
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