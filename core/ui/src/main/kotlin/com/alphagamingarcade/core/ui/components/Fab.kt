package com.alphagamingarcade.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

/**
 * Composable function that represents the floating action button in the Jetpack application.
 *
 * @param icon The icon to be displayed on the floating action button.
 * @param text The text to be displayed on the floating action button.
 * @param onClick Callback when the floating action button is clicked.
 * @param modifier Modifier to be applied to the floating action button.
 */
@Composable
fun JetpackExtendedFab(
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(icon, stringResource(text)) },
        text = { Text(text = stringResource(text)) },
        modifier = modifier,
    )
}
