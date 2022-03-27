package me.nemiron.khinkalyator.features.people.person.ui

import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.people.domain.PersonId

interface PersonComponent {

    val nameInputControl: InputControl

    val phoneInputControl: InputControl

    val buttonText: LocalizedString

    fun setConfiguration(config: Configuration)

    fun onSubmitClick()

    sealed interface Configuration {
        class NewPerson : Configuration

        class EditPerson(val personId: PersonId) : Configuration
    }

    sealed interface Output {
        object PersonEdited : Output
    }
}