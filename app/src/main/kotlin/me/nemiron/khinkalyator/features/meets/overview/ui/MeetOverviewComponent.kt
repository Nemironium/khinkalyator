package me.nemiron.khinkalyator.features.meets.overview.ui

import com.arkivanov.decompose.router.stack.ChildStack
import me.nemiron.khinkalyator.features.meets.check.ui.MeetCheckComponent
import me.nemiron.khinkalyator.features.meets.details.ui.MeetDetailsComponent
import me.nemiron.khinkalyator.features.meets.pager.ui.MeetPagerComponent

interface MeetOverviewComponent {

    val childStack: ChildStack<*, Child>

    val checkComponent: MeetCheckComponent?

    fun onCheckDismissed()

    sealed interface Child {
        class Pager(val component: MeetPagerComponent) : Child
        class Details(val component: MeetDetailsComponent) : Child
    }

    sealed interface Output {
        object MeetCloseRequested : Output
    }
}