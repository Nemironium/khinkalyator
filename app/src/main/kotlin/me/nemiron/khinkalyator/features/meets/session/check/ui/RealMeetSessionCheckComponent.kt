package me.nemiron.khinkalyator.features.meets.session.check.ui

import com.arkivanov.decompose.ComponentContext

class RealMeetSessionCheckComponent(
    componentContext: ComponentContext,
    configuration: MeetSessionCheckComponent.Configuration,
    onOutput: (MeetSessionCheckComponent.Output) -> Unit
) : MeetSessionCheckComponent, ComponentContext by componentContext