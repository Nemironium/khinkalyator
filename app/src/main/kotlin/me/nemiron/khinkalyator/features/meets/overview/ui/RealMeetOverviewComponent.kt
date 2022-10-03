package me.nemiron.khinkalyator.features.meets.overview.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.ComponentHolder
import me.nemiron.khinkalyator.core.componentHolder
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.log
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.meets.createMeetCheckComponent
import me.nemiron.khinkalyator.features.meets.createMeetDetailsComponent
import me.nemiron.khinkalyator.features.meets.createMeetPagerComponent
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.features.meets.check.ui.MeetCheckComponent
import me.nemiron.khinkalyator.features.meets.details.ui.MeetDetailsComponent
import me.nemiron.khinkalyator.features.meets.domain.ArchiveMeetUseCase
import me.nemiron.khinkalyator.features.meets.domain.DeleteMeetUseCase
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetUseCase
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetPagerComponent

class RealMeetOverviewComponent(
    componentContext: ComponentContext,
    private val meetId: MeetId,
    private val onOutput: (MeetOverviewComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    observeMeet: ObserveMeetUseCase,
    private val deleteMeet: DeleteMeetUseCase,
    private val archiveMeet: ArchiveMeetUseCase
) : MeetOverviewComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val checkComponentHolder: ComponentHolder<MeetCheckComponent.Configuration, MeetCheckComponent> =
        componentHolder(
            key = "CheckComponent",
            removeOnBackPressed = true
        ) { configuration, componentContext ->
            componentFactory.createMeetCheckComponent(
                componentContext,
                configuration,
                onOutput = ::onCheckOutput
            )
        }

    private val meetState by observeMeet(meetId).toComposeState(
        initialValue = null,
        coroutineScope = coroutineScope
    )

    private val status by derivedStateOf {
        meetState?.status ?: Meet.Status.Default
    }

    override val childStack: ChildStack<*, MeetOverviewComponent.Child> by childStack(
        source = navigation,
        initialConfiguration = ChildConfiguration.Pager(
            MeetPagerComponent.Page.People,
            meetId
        ),
        handleBackButton = true,
        childFactory = ::createChild
    ).log("MeetOverview").toComposeState(lifecycle)

    override val checkComponent: MeetCheckComponent?
        get() = checkComponentHolder.component

    override fun onCheckDismissed() {
        checkComponentHolder.configuration = null
    }

    private fun createChild(
        config: ChildConfiguration,
        componentContext: ComponentContext
    ): MeetOverviewComponent.Child =
        when (config) {
            is ChildConfiguration.Details -> MeetOverviewComponent.Child.Details(
                componentFactory.createMeetDetailsComponent(
                    componentContext,
                    config.detailsConfiguration
                )
            )
            is ChildConfiguration.Pager -> MeetOverviewComponent.Child.Pager(
                componentFactory.createMeetPagerComponent(
                    componentContext,
                    config.meetId,
                    config.initialPage,
                    onOutput = ::onPagerOutput
                )
            )
        }

    private fun onPagerOutput(output: MeetPagerComponent.Output) {
        when (output) {
            is MeetPagerComponent.Output.CheckRequested -> {
                val configuration = when (status) {
                    is Meet.Status.Active -> MeetCheckComponent.Configuration.Active(
                        meetId
                    )
                    is Meet.Status.Archived -> MeetCheckComponent.Configuration.Archived(
                        meetId
                    )
                }
                checkComponentHolder.configuration = configuration
            }
            is MeetPagerComponent.Output.DeleteRequested -> {
                coroutineScope.launch {
                    deleteMeet(meetId)
                    coroutineScope.cancel()
                    onOutput(MeetOverviewComponent.Output.MeetCloseRequested)
                }
            }
            is MeetPagerComponent.Output.DishDetailsRequested -> {
                if (status is Meet.Status.Active) {
                    navigation.push(
                        ChildConfiguration.Details(
                            MeetDetailsComponent.Configuration.DishDetails(
                                meetId = meetId,
                                dishId = output.dishId
                            )
                        )
                    )
                } else {
                    // TODO: show error message like snack
                }
            }
            is MeetPagerComponent.Output.PersonDetailsRequested -> {
                if (status is Meet.Status.Active) {
                    navigation.push(
                        ChildConfiguration.Details(
                            MeetDetailsComponent.Configuration.PersonDetails(
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

    private fun onCheckOutput(output: MeetCheckComponent.Output) {
        when (output) {
            MeetCheckComponent.Output.MeetEndRequested -> {
                coroutineScope.launch {
                    checkComponentHolder.configuration = null
                    // TODO: add tips from CheckComponent
                    archiveMeet(meetId, null)
                    coroutineScope.cancel()
                    onOutput(MeetOverviewComponent.Output.MeetCloseRequested)
                }
            }
        }
    }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        class Pager(
            val initialPage: MeetPagerComponent.Page,
            val meetId: MeetId
        ) : ChildConfiguration

        @Parcelize
        class Details(
            val detailsConfiguration: MeetDetailsComponent.Configuration
        ) : ChildConfiguration
    }
}