package me.nemiron.khinkalyator.features.people

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.features.emoji.domain.EmojiProvider
import me.nemiron.khinkalyator.features.people.data.DatabasePeopleStorage
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.GetPersonByIdUseCase
import me.nemiron.khinkalyator.features.people.domain.DeletePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.UpdatePersonUseCase
import me.nemiron.khinkalyator.features.people.home_page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.features.people.home_page.ui.RealPeoplePageComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.people.person.ui.RealPersonComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val peopleModule = module {
    singleOf(::DatabasePeopleStorage) { bind<PeopleStorage>() }
    singleOf(::EmojiProvider)
    factoryOf(::AddPersonUseCase)
    factoryOf(::GetPersonByIdUseCase)
    factoryOf(::DeletePersonUseCase)
    factoryOf(::ObserveActivePeopleUseCase)
    factoryOf(::UpdatePersonUseCase)
}

fun ComponentFactory.createPeoplePageComponent(
    componentContext: ComponentContext,
    onOutput: (PeoplePageComponent.Output) -> Unit
): PeoplePageComponent {
    return RealPeoplePageComponent(componentContext, onOutput, get(), get())
}

fun ComponentFactory.createPersonComponent(
    componentContext: ComponentContext,
    configuration: PersonComponent.Configuration,
    onOutput: (PersonComponent.Output) -> Unit
): PersonComponent {
    return RealPersonComponent(componentContext, configuration, onOutput, get(), get(), get())
}