package com.alphagamingarcade.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.R

/**
 * A Jetpack Compose top app bar with a title, navigation icon, and action icon.
 *
 * @param titleRes The string resource ID for the title of the top app bar.
 * @param navigationIcon The navigation icon to be displayed on the top app bar.
 * @param navigationIconContentDescription The content description for the navigation icon.
 * @param actionIcon The action icon to be displayed on the top app bar.
 * @param actionIconContentDescription The content description for the action icon.
 * @param modifier The modifier for this top app bar.
 * @param colors The colors for this top app bar.
 * @param onNavigationClick The callback when the navigation icon is clicked.
 * @param onActionClick The callback when the action icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgamgTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String?,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier,
    )
}

/**
 * A Jetpack Compose top app bar with a title and action icon.
 *
 * @param titleRes The string resource ID for the title of the top app bar.
 * @param actionIcon The action icon to be displayed on the top app bar.
 * @param actionIconContentDescription The content description for the action icon.
 * @param modifier The modifier for this top app bar.
 * @param colors The colors for this top app bar.
 * @param onActionClick The callback when the action icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgamgTopAppBar(
    @StringRes titleRes: Int,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier,
    )
}

/**
 * A Jetpack Compose top app bar with a title and avatar.
 *
 * @param titleRes The string resource ID for the title of the top app bar.
 * @param avatarUri The URI for the avatar image.
 * @param avatarContentDescription The content description for the avatar.
 * @param modifier The modifier for this top app bar.
 * @param colors The colors for this top app bar.
 * @param onAvatarClick The callback when the avatar is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgamgTopAppBarWithAvatar(
    @StringRes titleRes: Int,
    avatarUri: String?,
    avatarContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onAvatarClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        actions = {
            IconButton(onClick = onAvatarClick) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = avatarContentDescription,
                    placeholder = painterResource(id = R.drawable.ic_avatar),
                    fallback = painterResource(id = R.drawable.ic_avatar),
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                )
            }
        },
        colors = colors,
        modifier = modifier,
    )
}

/**
 * A Jetpack Compose top app bar with a title, navigation icon, and action button.
 *
 * @param titleRes The string resource ID for the title of the top app bar.
 * @param actionRes The string resource ID for the action button.
 * @param onActionClick The callback when the action button is clicked.
 * @param onNavigateBackClick The callback when the navigation icon is clicked.
 * @param colors The colors for this top app bar.
 * @param modifier The modifier for this top app bar.
 */
@Composable
fun AgamgActionBar(
    @StringRes titleRes: Int,
    @StringRes actionRes: Int,
    onActionClick: () -> Unit,
    onNavigateBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = onNavigateBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                )
            }
        },
        actions = {
            JetpackButton(onClick = onActionClick, modifier = Modifier.padding(end = 16.dp)) {
                Text(text = stringResource(id = actionRes))
            }
        },
        colors = colors,
        modifier = modifier,
    )
}

/**
 * A Jetpack Compose top app bar with an app logo on the left,
 * and a notification icon with avatar on the right.
 *
 * @param logoRes The drawable resource ID for the app logo.
 * @param logoContentDescription The content description for the app logo.
 * @param avatarUri The URI for the avatar image.
 * @param avatarContentDescription The content description for the avatar.
 * @param modifier The modifier for this top app bar.
 * @param colors The colors for this top app bar.
 * @param hasUnreadNotifications Whether to show the notification badge dot.
 * @param onNotificationClick The callback when the notification icon is clicked.
 * @param onAvatarClick The callback when the avatar is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgamgTopAppBarWithLogoAndActions(
    @DrawableRes logoRes: Int,
    logoContentDescription: String?,
    avatarUri: String?,
    avatarContentDescription: String?,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    hasUnreadNotifications: Boolean = false,
    onNotificationClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
) {
    TopAppBar(
        navigationIcon = {
            // Put logo here instead — this slot has no clipping issues
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = logoContentDescription,
                modifier = Modifier
                    .height(36.dp)
                    .widthIn(min = 40.dp, max = 140.dp)
                    .padding(start = 16.dp),
                contentScale = ContentScale.Fit,
            )
        },
        title = {},
        actions = {
            // Notification Icon with optional badge
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                    )
                }
                if (hasUnreadNotifications) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.error,
                                shape = CircleShape,
                            )
                            .align(Alignment.TopEnd)
                            .offset(x = (-40).dp, y = 10.dp),
                    )
                }
            }

            // Avatar
            IconButton(
                onClick = onAvatarClick,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = avatarContentDescription,
                    placeholder = painterResource(id = R.drawable.ic_avatar),
                    fallback = painterResource(id = R.drawable.ic_avatar),
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            actionIconContentColor = Color.Black, // so icons are visible on white
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AgamgTopAppBarWithLogoAndActionsPreview() {
    AgamgTopAppBarWithLogoAndActions(
        logoRes = R.drawable.ic_avatar,
        logoContentDescription = "App Logo",
        avatarUri = null, // will show fallback ic_avatar
        avatarContentDescription = "User Avatar",
        hasUnreadNotifications = false,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "With Notification Badge")
@Composable
fun AgamgTopAppBarWithLogoAndActionsWithBadgePreview() {
    AgamgTopAppBarWithLogoAndActions(
        logoRes = R.drawable.ic_avatar,
        logoContentDescription = "App Logo",
        avatarUri = null,
        avatarContentDescription = "User Avatar",
        hasUnreadNotifications = true, // shows red dot
    )
}