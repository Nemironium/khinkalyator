package me.nemiron.khinkalyator.root.ui

import com.arkivanov.decompose.router.stack.ChildStack
import me.nemiron.khinkalyator.features.home.ui.HomeComponent
import me.nemiron.khinkalyator.features.meets.create.ui.CreateMeetComponent
import me.nemiron.khinkalyator.features.meets.overview.ui.MeetOverviewComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant_overview.ui.RestaurantOverviewComponent
import me.nemiron.khinkalyator.root.ui.start.StartComponent

interface RootComponent {
    val childStack: ChildStack<*, Child>

    sealed interface Child {
        class Start(val component: StartComponent) : Child
        class Home(val component: HomeComponent) : Child
        class CreateMeet(val component: CreateMeetComponent) : Child
        class Meet(val component: MeetOverviewComponent) : Child
        class Restaurant(val component: RestaurantOverviewComponent) : Child
    }
}