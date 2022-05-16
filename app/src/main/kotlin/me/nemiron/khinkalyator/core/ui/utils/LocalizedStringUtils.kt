package me.nemiron.khinkalyator.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import me.aartikov.sesame.localizedstring.LocalizedString

/**
 * Returns LocalizedString value in current Locale.
 * When Locale changes, the value is updated automatically
 */
@Composable
fun LocalizedString.resolve(): String {
    LocalConfiguration.current // To automatically update when Locale changes
    return resolve(LocalContext.current).toString()
}