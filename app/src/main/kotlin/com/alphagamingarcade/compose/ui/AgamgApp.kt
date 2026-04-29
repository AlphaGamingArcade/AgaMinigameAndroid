package com.alphagamingarcade.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.alphagamingarcade.compose.R
import com.alphagamingarcade.compose.navigation.AgamgNavHost
import com.alphagamingarcade.compose.navigation.TopLevelDestination
import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.core.ui.components.AppBackground
import com.alphagamingarcade.core.ui.components.AppGradientBackground
import com.alphagamingarcade.core.ui.components.AgamgNavigationSuiteScaffold
import com.alphagamingarcade.core.ui.components.AgamgNavigationSuiteScope
import com.alphagamingarcade.core.ui.components.AgamgTopAppBarWithLogoAndActions
import com.alphagamingarcade.core.ui.theme.GradientColors
import com.alphagamingarcade.core.ui.theme.LocalGradientColors
import com.alphagamingarcade.core.ui.utils.CurrencyFormatter
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.settings.ui.SettingsDialog

/**
 * Composable function that represents the Jetpack Compose application.
 *
 * @param appState The state of the Jetpack application.
 * @param modifier The modifier to be applied to the Jetpack application.
 * @param windowAdaptiveInfo The window adaptive information.
 */
@Composable
fun AgamgApp(
    appState: AgamgAppState,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
//    val shouldShowGradientBackground =
//        appState.currentTopLevelDestination == TopLevelDestination.HOME

    val shouldShowGradientBackground = true

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    AppBackground(modifier = modifier) {
        AppGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            // If user is not connected to the internet show a snack bar to inform them.
            val notConnectedMessage = stringResource(R.string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = notConnectedMessage,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }

            AgamgApp(
                appState = appState,
                snackbarHostState = snackbarHostState,
                showSettingsDialog = showSettingsDialog,
                onDismissSettings = { showSettingsDialog = false },
                onTopAppBarActionClick = { showSettingsDialog = true },
                windowAdaptiveInfo = windowAdaptiveInfo,
                isDarkTheme = isDarkTheme
            )
        }
    }
}

/**
 * Composable function that represents the Jetpack Compose application.
 *
 * @param appState The state of the Jetpack application.
 * @param snackbarHostState The state of the snack bar host.
 * @param showSettingsDialog Flag to show the settings dialog.
 * @param onDismissSettings Callback when the settings dialog is dismissed.
 * @param onTopAppBarActionClick Callback when the top app bar action is clicked.
 * @param modifier The modifier to be applied to the Jetpack application.
 * @param windowAdaptiveInfo The window adaptive information.
 */
@Composable
private fun AgamgApp(
    appState: AgamgAppState,
    isDarkTheme: Boolean,
    snackbarHostState: SnackbarHostState,
    showSettingsDialog: Boolean,
    onDismissSettings: () -> Unit,
    onTopAppBarActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    // Collect the state of top-level destinations with unread resources
    val unreadDestinations by appState.topLevelDestinationsWithUnreadResources.collectAsStateWithLifecycle()

    // Get the current navigation destination
    val currentDestination = appState.currentDestination

    val context = LocalContext.current

    // Show the settings dialog if the flag is set
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { onDismissSettings() },
            onShowSnackbar = { message, action, throwable ->
                val actionPerformed = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = context.getString(action.actionText),
                    duration = SnackbarDuration.Short,
                ) == SnackbarResult.ActionPerformed
                if (actionPerformed && action == SnackbarAction.REPORT) {
                    throwable?.let {
//                        appState.crashReporter.reportException(throwable)
                    }
                }
                actionPerformed
            },
        )
    }

    // If there is no top-level destination, show the main scaffold
    if (appState.currentTopLevelDestination == null) {
        AgamgScaffold(
            appState = appState,
            snackbarHostState = snackbarHostState,
            onTopAppBarActionClick = onTopAppBarActionClick,
            modifier = modifier,
            isDarkTheme = isDarkTheme
        )
        return
    }

    // Otherwise, show the navigation suite scaffold with navigation items
    AgamgNavigationSuiteScaffold(
        navigationSuiteItems = {
            navigationItems(
                appState = appState,
                unreadDestinations = unreadDestinations,
                currentDestination = currentDestination,
            )
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
        AgamgScaffold(
            appState = appState,
            snackbarHostState = snackbarHostState,
            onTopAppBarActionClick = onTopAppBarActionClick,
            modifier = modifier,
            isDarkTheme = isDarkTheme,
        )
    }
}

/**
 * Composable function that represents the Jetpack Compose scaffold.
 *
 * @param appState The state of the Jetpack application.
 * @param snackbarHostState The state of the snack bar host.
 * @param onTopAppBarActionClick Callback when the top app bar action is clicked.
 * @param modifier The modifier to be applied to the Jetpack scaffold.
 */
@Composable
private fun AgamgScaffold(
    appState: AgamgAppState,
    isDarkTheme: Boolean,
    snackbarHostState: SnackbarHostState,
    onTopAppBarActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            // Show the top app bar on top level destinations.
            val destination = appState.currentTopLevelDestination
            var shouldShowTopAppBar = false

            if (destination != null) {
                shouldShowTopAppBar = true


                AgamgTopAppBarWithLogoAndActions(
                    logoRes = if (isDarkTheme) R.drawable.ic_app_logo_dark else R.drawable.ic_app_logo,
                    logoContentDescription = "App Logo",
                    isLoggedIn = appState.isUserLoggedIn,
                    balance = CurrencyFormatter.format(appState.userState.balance, appState.userState.currency),
                    onSettingsClick = { onTopAppBarActionClick() },
                )
            }

            Box(
                // Workaround for https://issuetracker.google.com/338478720
                modifier = Modifier.consumeWindowInsets(
                    if (shouldShowTopAppBar) {
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    } else {
                        WindowInsets(0, 0, 0, 0)
                    },
                ),
            ) {
                val context = LocalContext.current
                AgamgNavHost(
                    appState = appState,
                    onShowSnackbar = { message, action, throwable ->
                        val actionPerformed = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = context.getString(action.actionText),
                            duration = SnackbarDuration.Short,
                        ) == SnackbarResult.ActionPerformed
                        if (actionPerformed && action == SnackbarAction.REPORT) {
                            throwable?.let {
//                                appState.crashReporter.reportException(throwable)
                            }
                        }
                        actionPerformed
                    },
                )
            }
            // TODO: We may want to add padding or spacer when the snackbar is shown so that
            //  content doesn't display behind it.
        }
    }
}

/**
 * Composable function that represents the navigation items in the Jetpack Navigation Suite.
 *
 * @param appState The state of the Jetpack application.
 * @param unreadDestinations The set of top-level destinations with unread resources.
 * @param currentDestination The current navigation destination.
 */
private fun AgamgNavigationSuiteScope.navigationItems(
    appState: AgamgAppState,
    unreadDestinations: Set<TopLevelDestination>,
    currentDestination: NavDestination?,
) {
    appState.topLevelDestinations.forEach { destination ->
        val hasUnread = unreadDestinations.contains(destination)
        val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

        navigationItem(
            destination = destination,
            selected = selected,
            hasUnread = hasUnread,
            onNavigate = { appState.navigateToTopLevelDestination(destination) },
        )
    }
}

/**
 * Composable function that represents a navigation item in the Jetpack Navigation Suite.
 *
 * @param destination The top-level destination to navigate to.
 * @param selected Flag to indicate if the item is selected.
 * @param hasUnread Flag to indicate if the item has unread resources.
 * @param onNavigate Callback when the item is clicked.
 */
private fun AgamgNavigationSuiteScope.navigationItem(
    destination: TopLevelDestination,
    selected: Boolean,
    hasUnread: Boolean,
    onNavigate: () -> Unit,
) {
    item(
        selected = selected,
        onClick = onNavigate,
        icon = {
            Icon(
                imageVector = destination.unselectedIcon,
                contentDescription = null,
            )
        },
        selectedIcon = {
            Icon(
                imageVector = destination.selectedIcon,
                contentDescription = null,
            )
        },
        label = { Text(stringResource(destination.iconTextId)) },
        modifier = Modifier.then(
            if (hasUnread) Modifier.notificationDot() else Modifier,
        ),
    )
}

/**
 * Extension function for Modifier to add a notification dot.
 *
 * This function draws a small circle (dot) on the top-right corner of the content.
 *
 * @return A Modifier with the notification dot applied.
 */
@Suppress("ktlint:compose:modifier-composed-check")
// TODO: Fix issues with composed modifier lint rules
private fun Modifier.notificationDot(): Modifier = composed {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    drawWithContent {
        drawContent()
        drawCircle(
            tertiaryColor,
            radius = 5.dp.toPx(),
            // This is based on the dimensions of the NavigationBar's "indicator pill";
            // however, its parameters are private, so we must depend on them implicitly
            // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
            center = center + Offset(
                64.dp.toPx() * .45f,
                32.dp.toPx() * -.45f - 6.dp.toPx(),
            ),
        )
    }
}

/**
 * Extension function to check if the current NavDestination is a top-level destination.
 *
 * @param destination The top-level destination to check against.
 * @return True if the current NavDestination is in the hierarchy of the given top-level destination, false otherwise.
 */
private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) == true
    } == true