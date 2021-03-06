package me.nemiron.khinkalyator.features.meets.page.ui

import kotlinx.datetime.LocalDate
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData
import me.nemiron.khinkalyator.features.initials.ui.toInitialsViewData
import me.nemiron.khinkalyator.features.meets.domain.Meet
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import kotlin.math.absoluteValue

data class MeetFullViewData(
    val id: MeetId,
    val date: LocalizedString,
    val title: LocalizedString,
    val subtitle: LocalizedString,
    val initials: List<InitialsViewData>,
    val isActive: Boolean
)

fun Meet.toMeetFullViewData(): MeetFullViewData {
    val date = when (type) {
        is Meet.Type.Active -> LocalizedString.resource(R.string.meets_today_date)
        is Meet.Type.Archived -> type.dateTime.date.format()
    }

    return MeetFullViewData(
        id = id,
        date = date,
        title = LocalizedString.raw(restaurant.name),
        subtitle = LocalizedString.raw(restaurant.address?.value.orEmpty()),
        initials = persons.toInitialsViewData(),
        isActive = type is Meet.Type.Active
    )
}

private fun LocalDate.format(): LocalizedString {
    return LocalizedString.resource(
        R.string.meets_date_format,
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