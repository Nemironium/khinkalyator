package me.nemiron.khinkalyator.features.meets.create.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.features.meets.create.ui.KhinkaliButtonState

@Composable
fun KhinkaliButton(
    state: KhinkaliButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*
    * TODO: add different animations for each KhinkaliButtonState changing
    * */
    val iconRes = when (state) {
        KhinkaliButtonState.Invisible -> null
        KhinkaliButtonState.OneKhinkali -> R.drawable.ic_khinkali_48
        KhinkaliButtonState.TwoKhinkali -> R.drawable.img_khinkali_double_1
        KhinkaliButtonState.TwoKhinkaliJoy -> R.drawable.img_khinkali_double_2
    }

    iconRes?.let { resId ->
        KhContainedButton(
            modifier = modifier,
            iconRes = resId,
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun KhinkaliButtonPreview() {
    KhinkalyatorTheme {
        KhinkaliButton(
            state = KhinkaliButtonState.TwoKhinkali,
            onClick = { }
        )
    }
}