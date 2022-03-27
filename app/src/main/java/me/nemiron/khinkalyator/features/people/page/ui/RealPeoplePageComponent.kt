package me.nemiron.khinkalyator.features.people.page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.aartikov.sesame.dialog.DialogControl
import me.aartikov.sesame.dialog.show
import me.nemiron.khinkalyator.core.ui.CloseKeyboardService
import me.nemiron.khinkalyator.core.ui.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.ui.utils.dismissOnBackPressed
import me.nemiron.khinkalyator.core.ui.utils.toComposeState
import me.nemiron.khinkalyator.features.emoji.domain.EmojiProvider
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.DeletePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.ObservePersonUseCase
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

    override val personComponent = RealPersonComponent(
        childContext(key = "person"),
        onOutput = ::onPersonComponentOutput,
        observePerson = ObservePersonUseCase(peopleStorage),
        addPerson = AddPersonUseCase(peopleStorage, emojiProvider),
        updatePerson = UpdatePersonUseCase(peopleStorage)
    )

    override val personDialogControl = DialogControl<Unit, Unit>()

    private val personDialogControlState by personDialogControl
        .stateFlow
        .toComposeState(coroutineScope)

    private val isPersonDialogClosed by derivedStateOf {
        personDialogControlState is DialogControl.State.Hidden
    }

    override val closeKeyboardEvents = closeKeyboardService.closeKeyboardEventsFlow

    override val peopleViewData by derivedStateOf {
        peopleState.map(Person::toPersonFullViewData)
    }

    init {
        personDialogControl.dismissOnBackPressed(this)
        lifecycle.doOnCreate {
            snapshotFlow { isPersonDialogClosed }
                .onEach { isDialogClosed ->
                    if (isDialogClosed) {
                        closeKeyboardService.closeKeyboard()
                    }
                }
                .launchIn(coroutineScope)
        }
        personDialogControl.stateFlow
    }

    override fun onPersonAddClick() {
        if (isPersonDialogClosed) {
            personComponent.setConfiguration(PersonComponent.Configuration.NewPerson())
            personDialogControl.show()
        }
    }

    override fun onPersonDeleteClick(personId: PersonId) {
        coroutineScope.launch {
            deletePerson(personId)
        }
    }

    override fun onPersonClick(personId: PersonId) {
        if (isPersonDialogClosed) {
            personComponent.setConfiguration(PersonComponent.Configuration.EditPerson(personId))
            personDialogControl.show()
        }
    }

    private fun onPersonComponentOutput(output: PersonComponent.Output) = when (output) {
        PersonComponent.Output.PersonEdited -> {
            personDialogControl.dismiss()
        }
    }
}