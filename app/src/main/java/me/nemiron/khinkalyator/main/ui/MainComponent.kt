package me.nemiron.khinkalyator.main.ui

interface MainComponent {

    sealed interface Output {
        data class EveningSelected(val eveningId: Long) : Output
    }
}