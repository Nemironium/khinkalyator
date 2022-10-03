package me.nemiron.khinkalyator.features.meets

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.create.ui.RealCreateMeetComponent
import me.nemiron.khinkalyator.common_data.DatabaseMeetsStorage
import me.nemiron.khinkalyator.features.meets.domain.CreateMeetUseCase
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetsUseCase
import me.nemiron.khinkalyator.features.meets.domain.ObserveMeetUseCase
import me.nemiron.khinkalyator.features.meets.home_page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.home_page.ui.RealMeetsPageComponent
import me.nemiron.khinkalyator.features.meets.check.ui.MeetCheckComponent
import me.nemiron.khinkalyator.features.meets.check.ui.RealMeetCheckComponent
import me.nemiron.khinkalyator.features.meets.details.ui.MeetDetailsComponent
import me.nemiron.khinkalyator.features.meets.details.ui.RealMeetDetailsComponent
import me.nemiron.khinkalyator.features.meets.domain.ArchiveMeetUseCase
import me.nemiron.khinkalyator.features.meets.domain.DeleteMeetUseCase
import me.nemiron.khinkalyator.features.meets.overview.ui.MeetOverviewComponent
import me.nemiron.khinkalyator.features.meets.overview.ui.RealMeetOverviewComponent
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetPagerComponent
import me.nemiron.khinkalyator.features.meets.pager.ui.RealMeetPagerComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val meetsModule = module {
    singleOf(::DatabaseMeetsStorage) { bind<MeetsStorage>() }
    factoryOf(::CreateMeetUseCase)
    factoryOf(::ArchiveMeetUseCase)
    factoryOf(::DeleteMeetUseCase)
    factoryOf(::ObserveMeetsUseCase)
    factoryOf(::ObserveMeetUseCase)
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

fun ComponentFactory.createMeetOverviewComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    onOutput: (MeetOverviewComponent.Output) -> Unit,
): MeetOverviewComponent {
    return RealMeetOverviewComponent(
        componentContext,
        meetId,
        onOutput,
        get(),
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createMeetDetailsComponent(
    componentContext: ComponentContext,
    configuration: MeetDetailsComponent.Configuration
): MeetDetailsComponent {
    return RealMeetDetailsComponent(componentContext, configuration)
}

fun ComponentFactory.createMeetPagerComponent(
    componentContext: ComponentContext,
    meetId: MeetId,
    page: MeetPagerComponent.Page,
    onOutput: (MeetPagerComponent.Output) -> Unit
): MeetPagerComponent {
    return RealMeetPagerComponent(
        componentContext,
        meetId,
        page,
        onOutput,
        get()
    )
}

fun ComponentFactory.createMeetCheckComponent(
    componentContext: ComponentContext,
    configuration: MeetCheckComponent.Configuration,
    onOutput: (MeetCheckComponent.Output) -> Unit
): MeetCheckComponent {
    return RealMeetCheckComponent(componentContext, configuration, onOutput)
}