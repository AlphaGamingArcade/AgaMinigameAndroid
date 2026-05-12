package com.alphagamingarcade.feature.browse.navigation

import android.R
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alphagamingarcade.feature.browse.ui.BrowseScreen
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Profile.
 */
@Serializable
data class Browse(val filter: String)

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToBrowseScreen(
    filter: String,
    navOptions: NavOptions? = null
) {
    navigate(Browse(filter = filter), navOptions)
}

// Your route definition — argument is non-nullable
@Serializable
data class BrowseRoute(
    val filter: String,
)

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.browseScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onGameClick: (String) -> Unit,
) {
    composable<Browse> { backStackEntry ->
        val route = backStackEntry.toRoute<BrowseRoute>();
        BrowseScreen(
            filter = route.filter,
            onShowSnackbar = onShowSnackbar,
            onGameClick = onGameClick
        )
    }
}