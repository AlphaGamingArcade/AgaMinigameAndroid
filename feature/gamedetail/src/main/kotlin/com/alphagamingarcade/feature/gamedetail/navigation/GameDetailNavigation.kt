package com.alphagamingarcade.feature.gamedetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.gamedetail.ui.GameDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class GameDetail(val gameId: String)


/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToGameDetailScreen(gameId: String, navOptions: NavOptions? = null) {
    navigate(GameDetail(gameId = gameId), navOptions)
}


/**
 * Sign in screen.
 *
 * @param onBackClick Callback when sign up is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.gameDetailScreen(
    isLoggedIn: Boolean,
    onBackClick: () -> Unit,
    onPlayClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<GameDetail> {
        GameDetailScreen(
            isLoggedIn = isLoggedIn,
            onBackClick = onBackClick,
            onPlayClick = onPlayClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}