package me.nemiron.khinkalyator.common_data.mapper

import kotlinx.datetime.LocalDateTime
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.model.toPrice
import kotlin.math.roundToInt

internal fun mapToMeetStatus(dateIsoString: String, type: Int): Meet.Status {
    val createDate = LocalDateTime.parse(dateIsoString)
    return when (type) {
        0 -> Meet.Status.Archived(createDate)
        1 -> Meet.Status.Active(createDate)
        else -> throw IllegalArgumentException("Not supported Meet type")
    }
}

internal fun mapToTips(tipsType: Int, tipsValue: Double) = when (tipsType) {
    0 -> null
    1 -> Meet.Tips.Fixed(tipsValue.toPrice())
    2 -> Meet.Tips.Percent(tipsValue.roundToInt())
    else -> throw IllegalArgumentException("Not supported Tips Type")
}

internal fun getTipsTypeValue(tips: Meet.Tips?): Pair<Int, Double> {
    return when (tips) {
        is Meet.Tips.Fixed -> 1 to tips.price.value
        is Meet.Tips.Percent -> 2 to tips.value.toDouble()
        null -> 0 to 0.0
    }
}