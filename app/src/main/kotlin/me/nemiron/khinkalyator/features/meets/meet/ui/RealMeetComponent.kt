package me.nemiron.khinkalyator.features.meets.meet.ui

import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetId

class RealMeetComponent(
    componentContext: ComponentContext,
    val meetId: MeetId
) : MeetComponent, ComponentContext by componentContext