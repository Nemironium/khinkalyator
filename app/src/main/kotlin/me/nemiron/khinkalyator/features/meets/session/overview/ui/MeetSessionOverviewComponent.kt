package me.nemiron.khinkalyator.features.meets.session.overview.ui

import com.arkivanov.decompose.router.stack.ChildStack
import me.nemiron.khinkalyator.features.meets.session.check.ui.MeetSessionCheckComponent
import me.nemiron.khinkalyator.features.meets.session.details.ui.MeetSessionDetailsComponent
import me.nemiron.khinkalyator.features.meets.session.pager.ui.MeetSessionPagerComponent

interface MeetSessionOverviewComponent {

    val childStackState: ChildStack<*, Child>

    val checkComponent: MeetSessionCheckComponent?

    fun onCheckDismissed()

    sealed interface Child {
        class Pager(val component: MeetSessionPagerComponent) : Child
        class Details(val component: MeetSessionDetailsComponent) : Child
    }

    sealed interface Output {
        object MeetSessionCloseRequested : Output
    }
}