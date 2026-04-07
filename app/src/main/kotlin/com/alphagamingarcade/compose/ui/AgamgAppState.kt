package com.alphagamingarcade.compose.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.alphagamingarcade.compose.navigation.TopLevelDestination
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.network.utils.NetworkState
import com.alphagamingarcade.core.network.utils.NetworkUtils
import com.alphagamingarcade.feature.games.navigation.navigateToGamesScreen
import com.alphagamingarcade.feature.browse.navigation.navigateToBrowseScreen
import com.alphagamingarcade.feature.favorite.navigation.navigateToFavoriteScreen
import com.alphagamingarcade.feature.home.navigation.navigateToItemScreen
import com.alphagamingarcade.feature.user.navigation.navigateToUserScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Remembers and creates an instance of [AgamgAppState].
 *
 * @param isUserLoggedIn Indicates if the user is logged in.
 * @param windowSizeClass The current window size class.
 * @param networkUtils Utility for network state management.
 * @param userProfilePictureUri The URI of the user's profile picture.
 * @param coroutineScope The coroutine scope for managing coroutines.
 * @param navController The navigation controller for managing navigation.
 * @return An instance of [AgamgAppState].
 */
@Composable
fun rememberAgamgAppState(
    isUserLoggedIn: Boolean,
    windowSizeClass: WindowSizeClass,
    networkUtils: NetworkUtils,
    userProfilePictureUri: String? = null,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AgamgAppState {
    return remember(
        isUserLoggedIn,
        userProfilePictureUri,
        navController,
        windowSizeClass,
        coroutineScope,
        networkUtils,
    ) {
        AgamgAppState(
            isUserLoggedIn = isUserLoggedIn,
            userProfilePictureUri = userProfilePictureUri,
            navController = navController,
            windowSizeClass = windowSizeClass,
            coroutineScope = coroutineScope,
            networkUtils = networkUtils,
        )
    }
}

/**
 * State holder class for the Jetpack Compose application.
 *
 * @property isUserLoggedIn Indicates if the user is logged in.
 * @property userProfilePictureUri The URI of the user's profile picture.
 * @property navController The navigation controller for managing navigation.
 * @property windowSizeClass The current window size class.
 * @param coroutineScope The coroutine scope for managing coroutines.
 * @param networkUtils Utility for network state management.
 */
@Stable
class AgamgAppState(
    val isUserLoggedIn: Boolean,
    val userProfilePictureUri: String?,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope,
    networkUtils: NetworkUtils,
) {
    /**
     * The previous navigation destination.
     */
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    /**
     * The current navigation destination.
     */
    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    /**
     * The current top-level navigation destination.
     */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    /**
     * Indicates if the application is offline.
     */
    val isOffline = networkUtils.getCurrentState()
        .map { it != NetworkState.CONNECTED }
        .stateInDelayed(false, coroutineScope)

    /**
     * List of top-level destinations.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * State flow of top-level destinations with unread resources.
     */
    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
        // TODO: Requires Implementation
        MutableStateFlow(setOf<TopLevelDestination>()).asStateFlow()

    /**
     * Navigates to the item screen with no item ID (creates new item).
     */
    fun navigateToItemScreen() {
        navController.navigateToItemScreen(null)
    }

    /**
     * Navigates to the specified top-level destination.
     *
     * @param topLevelDestination The top-level destination to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.GAMES -> navController.navigateToGamesScreen(topLevelNavOptions)
            TopLevelDestination.BROWSE -> navController.navigateToBrowseScreen(topLevelNavOptions)
            TopLevelDestination.FAVORITE -> navController.navigateToFavoriteScreen(topLevelNavOptions)
            TopLevelDestination.USER -> navController.navigateToUserScreen(topLevelNavOptions)
        }
    }
}
