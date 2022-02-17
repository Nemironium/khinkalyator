package me.nemiron.khinkalyator.evening.ui

import com.arkivanov.decompose.ComponentContext

class RealEveningComponent(
    componentContext: ComponentContext,
    private val eveningId: Long
) : EveningComponent, ComponentContext by componentContext