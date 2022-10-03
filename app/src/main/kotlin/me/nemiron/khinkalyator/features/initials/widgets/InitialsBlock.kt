package me.nemiron.khinkalyator.features.initials.widgets

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.common_domain.model.Emoji
import me.nemiron.khinkalyator.features.initials.ui.InitialsViewData

@Composable
fun InitialsBlock(
    initials: List<InitialsViewData>,
    modifier: Modifier = Modifier,
    @IntRange(from = 2) visibleCount: Int = 4
) {
    val initialsCount = initials.size
    val data = if (initialsCount <= visibleCount) {
        initials
    } else {
        val realCounts = visibleCount - 1
        val extraCounts = initialsCount - realCounts
        val lastViewData = initials.last().copy(initials = "+$extraCounts")
        initials.take(realCounts) + lastViewData
    }

    OverlaidInitials(
        modifier = modifier,
        initials = data
    )
}

@Composable
private fun OverlaidInitials(
    initials: List<InitialsViewData>,
    modifier: Modifier = Modifier
) {
    val initialsSize = 32.dp
    val overlayWidth = 9.dp
    val startPadding = initialsSize - overlayWidth

    Box(modifier) {
        initials.forEachIndexed { index, data ->
            InitialsWithBackground(
                modifier = Modifier.padding(start = startPadding * index),
                data = data,
                textStyle = MaterialTheme.appTypography.initialsSmall,
                backgroundSize = initialsSize,
                overlayColor = MaterialTheme.colors.background
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialsBlockPreview() {
    KhinkalyatorTheme {
        val data = listOf(
            InitialsViewData("КЕ", Emoji("🐨")),
            InitialsViewData("КВ", Emoji("🦄")),
            InitialsViewData("ЖВ", Emoji("🐰")),
            InitialsViewData("РЧ", Emoji("🐮")),
            InitialsViewData("ЭЗ", Emoji("🐼"))
        )
        InitialsBlock(data, visibleCount = 4)
    }
}