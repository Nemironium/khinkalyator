package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.stack.ChildStack
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.meet.ui.MeetComponent
import me.nemiron.khinkalyator.features.restaraunts.overview.ui.RestaurantOverviewComponent

interface RootComponent {
    val childStackState: ChildStack<*, Child>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class CreateMeet(val component: CreateMeetComponent) : Child
        class Meet(val component: MeetComponent) : Child
        class Restaurant(val component: RestaurantOverviewComponent) : Child
    }
}