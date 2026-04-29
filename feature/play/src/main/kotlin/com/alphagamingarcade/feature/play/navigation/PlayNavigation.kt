package com.alphagamingarcade.feature.play.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import kotlinx.serialization.Serializable
import com.alphagamingarcade.feature.play.ui.PlayScreen

/**
 * Contact Support route.
 */
@Serializable
data class Play(
    val playUrl: String,
    val gameName: String
)

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToPlayScreen(playUrl: String, gameName: String, navOptions: NavOptions? = null) {
    navigate(Play(playUrl, gameName), navOptions)
}

///**
// * Adds the Profile screen to the navigation graph.
// *
// * @param onShowSnackbar Lambda function to show a snackbar message.
// */
fun NavGraphBuilder.playScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit,
) {
    composable<Play> { backStackEntry ->
        val play: Play = backStackEntry.toRoute()

        PlayScreen(
            playUrl = play.playUrl,
            gameName = play.gameName,
            onShowSnackbar = onShowSnackbar,
            onBackClick = onBackClick,
        )
    }
}