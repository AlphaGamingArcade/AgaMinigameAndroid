package com.alphagamingarcade.feature.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.user.ui.user.UserScreen
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Profile.
 */
@Serializable
data object User


/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToUserScreen(navOptions: NavOptions?) {
    navigate(User, navOptions)
}

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToHistoryScreen(navOptions: NavOptions?) {
    navigate(User, navOptions)
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.userScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean
) {
    composable<User> {
        UserScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
