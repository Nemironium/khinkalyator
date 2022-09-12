package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.stack.ChildStack
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.session.overview.ui.MeetSessionOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui.RestaurantOverviewComponent

interface RootComponent {
    val childStackState: ChildStack<*, Child>

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class CreateMeet(val component: CreateMeetComponent) : Child
        class MeetSession(val component: MeetSessionOverviewComponent) : Child
        class Restaurant(val component: RestaurantOverviewComponent) : Child
    }
}