package me.nemiron.khinkalyator.home.ui

import com.arkivanov.decompose.router.RouterState
import me.nemiron.khinkalyator.meets.ui.MeetsComponent
import me.nemiron.khinkalyator.people.ui.PeopleComponent
import me.nemiron.khinkalyator.restaraunts.ui.RestaurantsComponent

interface HomeComponent {

    val routerState: RouterState<*, Child>

    val isTabsVisible: Boolean

    fun onPageSelected(page: Page)

    enum class Page {
        Meets, Restaurants, People
    }

    sealed interface Child {
        class Meets(val component: MeetsComponent) : Child
        class Restaurants(val component: RestaurantsComponent) : Child
        class People(val component: PeopleComponent) : Child
    }

    sealed interface Output
}