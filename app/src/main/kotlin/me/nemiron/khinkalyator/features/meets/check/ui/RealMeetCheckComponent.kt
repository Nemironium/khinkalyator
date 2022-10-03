package me.nemiron.khinkalyator.features.meets.check.ui

import com.arkivanov.decompose.ComponentContext

class RealMeetCheckComponent(
    componentContext: ComponentContext,
    configuration: MeetCheckComponent.Configuration,
    onOutput: (MeetCheckComponent.Output) -> Unit
) : MeetCheckComponent, ComponentContext by componentContext