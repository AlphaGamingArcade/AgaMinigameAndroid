package com.alphagamingarcade.feature.games.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.games.ui.categories.CategoriesScreen
import com.alphagamingarcade.feature.games.ui.games.GamesScreen
import kotlinx.serialization.Serializable


// ─── Routes ──────────────────────────────────────────────────────────────────

@Serializable
data object Games

@Serializable
data class Categories(val categoryId: String) // pass category ID or name

// ─── Games Navigation ────────────────────────────────────────────────────────

fun NavController.navigateToGamesScreen(navOptions: NavOptions? = null) {
    navigate(Games, navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onGameClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
) {
    composable<Games> {
        GamesScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = onGameClick,
            onCategoryClick = onCategoryClick,
        )
    }
}

// ─── Categories Navigation ───────────────────────────────────────────────────

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToCategoriesScreen(
    categoryId: String,
    navOptions: NavOptions? = null) {
    navigate(Categories(categoryId = categoryId), navOptions)
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.categoriesScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onGameClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<Categories> { backStackEntry ->
        val categories = backStackEntry.toRoute<Categories>();
        CategoriesScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = onGameClick,
            categoryName = categories.categoryId,
            onBackClick = onBackClick,
        )
    }
}