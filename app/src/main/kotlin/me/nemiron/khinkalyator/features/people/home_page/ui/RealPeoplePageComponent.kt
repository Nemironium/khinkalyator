package me.nemiron.khinkalyator.features.people.home_page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.people.domain.DeletePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId

class RealPeoplePageComponent(
    componentContext: ComponentContext,
    private val onOutput: (PeoplePageComponent.Output) -> Unit,
    observeActivePeople: ObserveActivePeopleUseCase,
    private val deletePerson: DeletePersonUseCase
) : PeoplePageComponent, ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    private val peopleState by observeActivePeople().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    override val peopleViewData by derivedStateOf {
        peopleState.map(Person::toPersonHomePageViewData)
    }

    override fun onPersonAddClick() {
        onOutput(PeoplePageComponent.Output.NewPersonRequested)
    }

    override fun onPersonDeleteClick(personId: PersonId) {
        coroutineScope.launch {
            deletePerson(personId)
        }
    }

    override fun onPersonClick(personId: PersonId) {
        onOutput(PeoplePageComponent.Output.PersonRequested(personId))
    }
}