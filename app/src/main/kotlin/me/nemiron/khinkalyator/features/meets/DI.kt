package me.nemiron.khinkalyator.features.meets

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.create.ui.RealCreateMeetComponent
import me.nemiron.khinkalyator.features.meets.meet.data.InMemoryMeetsStorage
import me.nemiron.khinkalyator.features.meets.meet.domain.CreateMeetUseCase
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetId
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetsStorage
import me.nemiron.khinkalyator.features.meets.meet.domain.ObserveMeetsUseCase
import me.nemiron.khinkalyator.features.meets.meet.ui.MeetComponent
import me.nemiron.khinkalyator.features.meets.meet.ui.RealMeetComponent
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.page.ui.RealMeetsPageComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val meetsModule = module {
    singleOf(::InMemoryMeetsStorage) { bind<MeetsStorage>() }
    factoryOf(::CreateMeetUseCase)
    factoryOf(::ObserveMeetsUseCase)
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

fun ComponentFactory.createMeetComponent(
    componentContext: ComponentContext,
    meetId: MeetId
): MeetComponent {
    return RealMeetComponent(componentContext, meetId)
}