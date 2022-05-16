package me.nemiron.khinkalyator.core.utils

import me.aartikov.sesame.compose.form.control.TextTransformation

object TextTransformations {
    val PhoneNumber = TextTransformation { it.replace(Regex("[^0-9+() -]"), "") }
    val PersonName = TextTransformation { it.replace(Regex("[^a-zA-Zа-яА-ЯёЁ `-]"), "") }
}