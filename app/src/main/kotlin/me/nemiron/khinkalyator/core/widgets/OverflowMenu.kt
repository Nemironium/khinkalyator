package me.nemiron.khinkalyator.core.widgets

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.utils.resolve

data class OverflowMenuData(
    val title: LocalizedString,
    val onMenuItemClick: () -> Unit
)

@Composable
fun OverflowMenu(vararg data:  OverflowMenuData) {
    var isExpanded by remember { mutableStateOf(false) }

    ActionButton(
        id = R.drawable.ic_overflow_24,
        onClick = { isExpanded = true }
    )
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
    ) {
        data.forEach { menuData ->
            DropdownMenuItem(onClick = menuData.onMenuItemClick) {
                Text(text = menuData.title.resolve())
            }

        }
    }
}