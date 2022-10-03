package me.nemiron.khinkalyator.features.meets.home_page.ui

import kotlinx.datetime.LocalDate
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.core.utils.isToday
import me.nemiron.khinkalyator.core.utils.isYesterday
import kotlin.math.absoluteValue

data class MeetHomePageViewData(
    val id: MeetId,
    val date: LocalizedString,
    val title: LocalizedString,
    val subtitle: LocalizedString,
    val initials: List<InitialsViewData>,
    val isActive: Boolean
)

fun Meet.toMeetHomePageViewData(): MeetHomePageViewData {
    val date = when {
        status.createTime.isToday() -> LocalizedString.resource(R.string.meets_home_page_today_date)
        status.createTime.isYesterday() -> LocalizedString.resource(R.string.meets_home_page_yesterday_date)
        else -> status.createTime.date.format()
    }

    return MeetHomePageViewData(
        id = id,
        date = date,
        title = LocalizedString.raw(restaurant.name),
        subtitle = LocalizedString.raw(restaurant.address?.value.orEmpty()),
        initials = people.toInitialsViewData(),
        isActive = status is Meet.Status.Active
    )
}

private fun LocalDate.format(): LocalizedString {
    return LocalizedString.resource(
        R.string.meets_home_page__date_format,
        dayOfMonth.toStringWithZero(),
        monthNumber.toStringWithZero(),
        year.toString()
    )
}

private fun Int.toStringWithZero(): String {
    val isZeroNeeded = this.absoluteValue < 10
    return if (isZeroNeeded) {
        "0$this"
    } else {
        toString()
    }
}