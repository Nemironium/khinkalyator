package me.nemiron.khinkalyator.features.home.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.nemiron.khinkalyator.core.ComponentHolder
import me.nemiron.khinkalyator.core.componentHolder
import me.nemiron.khinkalyator.core.ui.keyboard.CloseKeyboardService
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.features.emoji.domain.EmojiProvider
import me.nemiron.khinkalyator.features.meets.page.ui.MeetsPageComponent
import me.nemiron.khinkalyator.features.meets.page.ui.RealMeetsPageComponent
import me.nemiron.khinkalyator.features.people.domain.AddPersonUseCase
import me.nemiron.khinkalyator.features.people.domain.DeletePersonUseCase
import me.nemiron.khinkalyator.features.people.domain.GetPersonByIdUseCase
import me.nemiron.khinkalyator.features.people.domain.ObserveActivePeopleUseCase
import me.nemiron.khinkalyator.features.people.domain.PeopleStorage
import me.nemiron.khinkalyator.features.people.domain.UpdatePersonUseCase
import me.nemiron.khinkalyator.features.people.page.ui.PeoplePageComponent
import me.nemiron.khinkalyator.features.people.page.ui.RealPeoplePageComponent
import me.nemiron.khinkalyator.features.people.person.ui.PersonComponent
import me.nemiron.khinkalyator.features.people.person.ui.RealPersonComponent
import me.nemiron.khinkalyator.features.restaraunts.domain.ObserveRestaurantsUseCase
import me.nemiron.khinkalyator.features.restaraunts.domain.RestaurantsStorage
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RealRestaurantsPageComponent
import me.nemiron.khinkalyator.features.restaraunts.page.ui.RestaurantsPageComponent

class RealHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeComponent.Output) -> Unit,
    peopleStorage: PeopleStorage,
    restaurantsStorage: RestaurantsStorage,
    private val closeKeyboardService: CloseKeyboardService
) : HomeComponent, ComponentContext by componentContext {

    /*
    * TODO: think about separate lifecycle for page components
    * */

    private val coroutineScope = componentCoroutineScope()

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
                addPerson = AddPersonUseCase(peopleStorage, EmojiProvider()),
                updatePerson = UpdatePersonUseCase(peopleStorage)
            )
        }

    override val meetsPageComponent = RealMeetsPageComponent(
        childContext(key = "meetsPage"),
        onOutput = ::onMeetsOutput
    )

    override val restaurantsPageComponent = RealRestaurantsPageComponent(
        childContext(key = "restaurantsPage"),
        observeRestaurants = ObserveRestaurantsUseCase(restaurantsStorage),
        onOutput = ::onRestaurantsOutput
    )

    override val peoplePageComponent = RealPeoplePageComponent(
        childContext(key = "peoplePage"),
        observeActivePeople = ObserveActivePeopleUseCase(peopleStorage),
        deletePerson = DeletePersonUseCase(peopleStorage),
        onOutput = ::onPeopleOutput
    )

    override val personComponent: PersonComponent?
        get() = personComponentHolder.component

    override val closeKeyboardEvents = closeKeyboardService.closeKeyboardEventsFlow

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

    override fun onPersonDismissed() {
        personComponentHolder.configuration = null
    }

    private fun onMeetsOutput(output: MeetsPageComponent.Output) {
        when (output) {
            is MeetsPageComponent.Output.NewMeetRequested -> {
                onOutput(HomeComponent.Output.NewMeetRequested)
            }
            is MeetsPageComponent.Output.MeetRequested -> {
                // TODO
            }
        }
    }

    private fun onRestaurantsOutput(output: RestaurantsPageComponent.Output) {
        when (output) {
            is RestaurantsPageComponent.Output.NewRestaurantRequested -> {
                onOutput(HomeComponent.Output.NewRestaurantRequested)
            }
            is RestaurantsPageComponent.Output.RestaurantRequested -> {
                onOutput(HomeComponent.Output.RestaurantRequested(output.restaurantId))
            }
        }
    }

    private fun onPeopleOutput(output: PeoplePageComponent.Output) {
        when (output) {
            is PeoplePageComponent.Output.NewPersonRequested -> {
                personComponentHolder.configuration = PersonComponent.Configuration.NewPerson
            }
            is PeoplePageComponent.Output.PersonRequested -> {
                personComponentHolder.configuration = PersonComponent.Configuration.EditPerson(
                    output.personId
                )
            }
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