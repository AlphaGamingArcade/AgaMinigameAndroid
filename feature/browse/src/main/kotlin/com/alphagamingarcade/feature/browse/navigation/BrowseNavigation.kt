package com.alphagamingarcade.feature.browse.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.feature.browse.ui.BrowseScreen
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Profile.
 */
@Serializable
data object Browse

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToBrowseScreen(
    navOptions: NavOptions?) {
    navigate(Browse, navOptions)
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.browseScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onGameClick: (String) -> Unit,
) {
    composable<Browse> {
        BrowseScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = onGameClick
        )
    }
}