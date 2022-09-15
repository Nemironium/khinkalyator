package me.nemiron.khinkalyator.features.meets.create.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.ComponentHolder
import me.nemiron.khinkalyator.core.componentHolder
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.meets.domain.CreateMeetUseCase
import me.nemiron.khinkalyator.features.people.createPersonComponent
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.ObserveActiveRestaurantsUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.RestaurantId

class RealCreateMeetComponent(
    componentContext: ComponentContext,
    private val onOutput: (CreateMeetComponent.Output) -> Unit,
    componentFactory: ComponentFactory,
    private val createMeet: CreateMeetUseCase,
    observeActiveRestaurants: ObserveActiveRestaurantsUseCase,
    observeActivePeople: ObserveActivePeopleUseCase
) : CreateMeetComponent, ComponentContext by componentContext {

    private var selectedRestaurantId: RestaurantId? by mutableStateOf(null)

    private var selectedPersonIds: List<PersonId> by mutableStateOf(emptyList())

    private val coroutineScope = componentCoroutineScope()

    private val restaurantsState by observeActiveRestaurants().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    private val peopleState by observeActivePeople().toComposeState(
        initialValue = emptyList(),
        coroutineScope = coroutineScope
    )

    private val personComponentHolder: ComponentHolder<PersonComponent.Configuration, PersonComponent> =
        componentHolder(
            key = "PersonComponent",
            removeOnBackPressed = true
        ) { configuration, componentContext ->
            componentFactory.createPersonComponent(
                componentContext,
                configuration,
                onOutput = ::onPersonComponentOutput
            )
        }

    override val personComponent: PersonComponent?
        get() = personComponentHolder.component

    override val restaurantsViewData by derivedStateOf {
        restaurantsState.map { restaurant ->
            val isSelected = restaurant.id == selectedRestaurantId
            restaurant.toRestaurantSimpleViewData(isSelected)
        }
    }

    override val peopleViewData by derivedStateOf {
        peopleState.map { person ->
            val isSelected = selectedPersonIds.contains(person.id)
            person.toPersonSimpleViewData(isSelected)
        }
    }

    override val buttonState by derivedStateOf {
        val selectedPersonIdsCount = selectedPersonIds.size
        val isRestaurantSelected = selectedRestaurantId != null

        when {
            isRestaurantSelected && selectedPersonIdsCount == 1 -> KhinkaliButtonState.OneKhinkali
            isRestaurantSelected && selectedPersonIdsCount == 2 -> KhinkaliButtonState.TwoKhinkali
            isRestaurantSelected && selectedPersonIdsCount > 2 -> KhinkaliButtonState.TwoKhinkaliJoy
            else -> KhinkaliButtonState.Invisible
        }
    }

    override fun onPersonDismissed() {
        personComponentHolder.configuration = null
    }

    override fun onRestaurantAddClick() {
        onOutput(CreateMeetComponent.Output.NewRestaurantRequested)
    }

    override fun onRestaurantClick(restaurantId: RestaurantId) {
        val isAlreadySelected = restaurantId == selectedRestaurantId

        selectedRestaurantId = if (isAlreadySelected) {
            null
        } else {
            restaurantId
        }
    }

    override fun onPersonAddClick() {
        personComponentHolder.configuration = PersonComponent.Configuration.NewPerson
    }

    override fun onPersonClick(personId: PersonId) {
        val selectedPersonIds = selectedPersonIds
        val isAlreadySelected = selectedPersonIds.contains(personId)

        this.selectedPersonIds = selectedPersonIds.toMutableList().apply {
            if (isAlreadySelected) {
                remove(personId)
            } else {
                add(personId)
            }
        }
    }

    override fun onCreateMeetClick() {
        val restaurantId = selectedRestaurantId ?: return
        val personIds = selectedPersonIds.takeIf { it.isNotEmpty() } ?: return

        coroutineScope.launch {
            val newMeetId = createMeet(restaurantId, personIds)
            onOutput(CreateMeetComponent.Output.MeetCreated(newMeetId))
        }
    }

    private fun onPersonComponentOutput(output: PersonComponent.Output) {
        when (output) {
            PersonComponent.Output.PersonEdited -> {
                personComponentHolder.configuration = null
            }
        }
    }
}