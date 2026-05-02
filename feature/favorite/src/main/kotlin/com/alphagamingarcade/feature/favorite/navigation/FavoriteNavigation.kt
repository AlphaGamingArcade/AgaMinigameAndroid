package com.alphagamingarcade.feature.favorite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.favorite.ui.favorite.FavoriteScreen
import com.alphagamingarcade.feature.favorite.ui.loginrequired.LoginRequiredScreen
import kotlinx.serialization.Serializable


/**
 * Serializable data object representing the Profile.
 */
@Serializable
data object Favorite

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToFavoriteScreen(navOptions: NavOptions?) {
    navigate(Favorite, navOptions)
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.favoriteScreen(
    isLoggedIn: Boolean,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onResumeGameClick: (String) -> Unit
) {
    composable<Favorite> {
        if (!isLoggedIn){
            LoginRequiredScreen(
                onShowSnackbar = onShowSnackbar,
                onLoginClick = onSignInClick,
                onSignUpClick = onSignUpClick
            )
        } else {
            FavoriteScreen(
                onShowSnackbar = onShowSnackbar,
                onResumeGameClick = onResumeGameClick
            )
        }
    }
}
