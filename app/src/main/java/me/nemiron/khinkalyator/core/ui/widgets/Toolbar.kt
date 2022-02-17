package me.nemiron.khinkalyator.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.TopAppBar
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.ui.utils.dispatchOnBackPressed
import me.nemiron.khinkalyator.core.ui.utils.statusBar

@Composable
fun Toolbar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = { BackButton() },
    actionIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.primary,
    statusBarModifier: Modifier = Modifier.statusBar(backgroundColor)
) {
    TopAppBar(
        modifier = modifier.then(statusBarModifier),
        title = { title?.let { Text(text = it) } },
        navigationIcon = navigationIcon,
        actions = { actionIcon?.invoke() },
        backgroundColor = backgroundColor,
        elevation = 0.dp
    )
}

@Composable
fun BackButton(modifier: Modifier = Modifier) {
    NavigationButton(iconRes = R.drawable.ic_back_24, modifier)
}

@Composable
fun CloseButton(modifier: Modifier = Modifier) {
    NavigationButton(iconRes = R.drawable.ic_close_24, modifier)
}

@Composable
fun NavigationButton(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    IconButton(onClick = { dispatchOnBackPressed(context) }, modifier = modifier) {
        Icon(
            painterResource(iconRes),
            contentDescription = null
        )
    }
}

@Composable
fun ActionButton(
    @DrawableRes id: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.onPrimary,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            tint = tint
        )
    }
}