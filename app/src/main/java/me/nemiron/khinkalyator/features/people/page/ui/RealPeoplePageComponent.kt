package me.nemiron.khinkalyator.features.people.page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.ui.CloseKeyboardService
import me.nemiron.khinkalyator.core.ui.ComponentHolder
import me.nemiron.khinkalyator.core.ui.componentHolder
import me.nemiron.khinkalyator.core.ui.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.ui.utils.toComposeState
import me.nemiron.khinkalyator.features.emoji.domain.EmojiProvider
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.DeletePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.GetPersonByIdUseCase
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.domain.UpdatePersonUseCase
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.people.person.ui.RealPersonComponent

class RealPeoplePageComponent(
    componentContext: ComponentContext,
    peopleStorage: PeopleStorage,
    private val closeKeyboardService: CloseKeyboardService,
    observeActivePeople: ObserveActivePeopleUseCase = ObserveActivePeopleUseCase(peopleStorage),
    private val deletePerson: DeletePersonUseCase = DeletePersonUseCase(peopleStorage),
    emojiProvider: EmojiProvider = EmojiProvider()
) : PeoplePageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val peopleState by observeActivePeople().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    private val personComponentHolder: ComponentHolder<PersonComponent.Configuration, PersonComponent> =
        componentHolder(
            key = "PersonComponent",
            removeOnBackPressed = true
        ) { configuration, componentContext ->
            RealPersonComponent(
                componentContext,
                configuration = configuration,
                onOutput = ::onPersonComponentOutput,
                getPersonById = GetPersonByIdUseCase(peopleStorage),
                addPerson = AddPersonUseCase(peopleStorage, emojiProvider),
                updatePerson = UpdatePersonUseCase(peopleStorage)
            )
        }

    override val personComponent: PersonComponent?
        get() = personComponentHolder.component

    override val closeKeyboardEvents = closeKeyboardService.closeKeyboardEventsFlow

    override val peopleViewData by derivedStateOf {
        peopleState.map(Person::toPersonFullViewData)
    }

    private val isPersonDialogClosed by derivedStateOf {
        personComponent == null
    }

    init {
        lifecycle.doOnCreate {
            snapshotFlow { isPersonDialogClosed }
                .onEach { isDialogClosed ->
                    if (isDialogClosed) {
                        closeKeyboardService.closeKeyboard()
                    }
                }
                .launchIn(coroutineScope)
        }
    }

    override fun onPersonAddClick() {
        personComponentHolder.configuration = PersonComponent.Configuration.NewPerson
    }

    override fun onPersonDeleteClick(personId: PersonId) {
        coroutineScope.launch {
            deletePerson(personId)
        }
    }

    override fun onPersonClick(personId: PersonId) {
        personComponentHolder.configuration = PersonComponent.Configuration.EditPerson(personId)
    }

    private fun onPersonComponentOutput(output: PersonComponent.Output) {
        return when (output) {
            PersonComponent.Output.PersonEdited -> {
                personComponentHolder.configuration = null
            }
        }
    }
}