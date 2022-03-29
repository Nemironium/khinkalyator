package me.nemiron.khinkalyator.features.people.person.ui

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.features.people.domain.PersonId

interface PersonComponent {

    val nameInputControl: InputControl

    val phoneInputControl: InputControl

    val buttonText: LocalizedString

    fun onSubmitClick()

    sealed interface Configuration : Parcelable {
        @Parcelize
        object NewPerson : Configuration

        @Parcelize
        class EditPerson(val personId: PersonId) : Configuration
    }

    sealed interface Output {
        object PersonEdited : Output
    }
}