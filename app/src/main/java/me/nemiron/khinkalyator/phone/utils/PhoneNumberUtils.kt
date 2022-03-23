package me.nemiron.khinkalyator.phone.utils

import me.nemiron.khinkalyator.phone.domain.Phone

fun Phone.format(): String {
    return value.formatToPhoneNumber()
}

private fun String.formatToPhoneNumber(): String {
    return this.replace(
        Regex("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})"),
        "$1 ($2) $3 - $4 - $5"
    )
}