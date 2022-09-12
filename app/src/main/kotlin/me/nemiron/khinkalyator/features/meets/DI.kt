package me.nemiron.khinkalyator.features.meets

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.create.ui.RealCreateMeetComponent
import me.nemiron.khinkalyator.features.meets.data.InMemoryMeetsStorage
import me.nemiron.khinkalyator.features.meets.domain.CreateMeetUseCase
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.meets.domain.MeetsStorage
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetsUseCase
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetSessionUseCase
import me.nemiron.khinkalyator.features.meets.home_page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.home_page.ui.RealMeetsPageComponent
import me.nemiron.khinkalyator.features.meets.meet_session_check.ui.MeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.meet_session_check.ui.RealMeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.meet_session_details.ui.MeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.meet_session_details.ui.RealMeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.meet_session_overview.ui.MeetSessionOverviewComponent
import me.nemiron.khinkalyator.features.meets.meet_session_overview.ui.RealMeetSessionOverviewComponent
import me.nemiron.khinkalyator.features.meets.meet_session_pager.ui.MeetSessionPagerComponent
import me.nemiron.khinkalyator.features.meets.meet_session_pager.ui.RealMeetSessionPagerComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val meetsModule = module {
    singleOf(::InMemoryMeetsStorage) { bind<MeetsStorage>() }
    factoryOf(::CreateMeetUseCase)
    factoryOf(::ObserveMeetsUseCase)
    factoryOf(::ObserveMeetSessionUseCase)
}

fun ComponentFactory.createMeetsPageComponent(
    componentContext: ComponentContext,
    onOutput: (MeetsPageComponent.Output) -> Unit
): MeetsPageComponent {
    return RealMeetsPageComponent(
        componentContext,
        onOutput,
        get()
    )
}

fun ComponentFactory.createCreateMeetComponent(
    componentContext: ComponentContext,
    onOutput: (CreateMeetComponent.Output) -> Unit
): CreateMeetComponent {
    return RealCreateMeetComponent(
        componentContext,
        onOutput,
        get(),
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createMeetSessionOverviewComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    onOutput: (MeetSessionOverviewComponent.Output) -> Unit,
): MeetSessionOverviewComponent {
    return RealMeetSessionOverviewComponent(
        componentContext,
        meetId,
        onOutput,
        get()
    )
}

fun ComponentFactory.createMeetSessionDetailsComponent(
    componentContext: ComponentContext,
    configuration: MeetSessionDetailsComponent.Configuration
): MeetSessionDetailsComponent {
    return RealMeetSessionDetailsComponent(componentContext, configuration)
}

fun ComponentFactory.createMeetSessionPagerComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    page: MeetSessionPagerComponent.Page,
    onOutput: (MeetSessionPagerComponent.Output) -> Unit
): MeetSessionPagerComponent {
    return RealMeetSessionPagerComponent(
        componentContext,
        meetId,
        page,
        onOutput,
        get()
    )
}

fun ComponentFactory.createMeetSessionCheckComponent(
    componentContext: ComponentContext,
    configuration: MeetSessionCheckComponent.Configuration,
    onOutput: (MeetSessionCheckComponent.Output) -> Unit
): MeetSessionCheckComponent {
    return RealMeetSessionCheckComponent(componentContext, configuration, onOutput)
}