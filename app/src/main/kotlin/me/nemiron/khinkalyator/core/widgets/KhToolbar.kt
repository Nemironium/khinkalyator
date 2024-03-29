package me.nemiron.khinkalyator.core.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.TopAppBar
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.contentKhColorFor
import me.nemiron.khinkalyator.core.utils.dispatchOnBackPressed
import me.nemiron.khinkalyator.core.utils.statusBar

@Composable
fun KhModalToolbar(
    title: String,
    logoIcon: @Composable (RowScope.() -> Unit),
    modifier: Modifier = Modifier,
    additionalTitle: String? = null,
    backgroundColor: Color = MaterialTheme.additionalColors.secondaryBackground,
    statusBarModifier: Modifier = Modifier.statusBar(backgroundColor, true)
) {
    val textColor = contentKhColorFor(backgroundColor)

    Row(
        modifier = modifier
            .background(backgroundColor)
            .then(statusBarModifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        logoIcon()
        Text(
            modifier = Modifier.weight(0.65f),
            text = title,
            style = MaterialTheme.appTypography.head2,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        additionalTitle?.let {
            Text(
                modifier = Modifier.weight(0.35f),
                text = additionalTitle,
                textAlign = TextAlign.End,
                style = MaterialTheme.appTypography.head2,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun KhToolbar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = { BackButton() },
    actionIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.primary,
    statusBarModifier: Modifier = Modifier.statusBar(backgroundColor)
) {
    TopAppBar(
        modifier = modifier.then(statusBarModifier),
        title = {
            title?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
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