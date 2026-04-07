package com.alphagamingarcade.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.profile.ui.ProfileScreen
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Profile.
 */
@Serializable
data object Profile

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToProfileScreen(navOptions: NavOptions?) {
    navigate(Profile, navOptions)
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.profileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Profile> {
        ProfileScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
