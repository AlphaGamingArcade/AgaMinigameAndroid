package com.alphagamingarcade.feature.categories.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.categories.ui.CategoriesScreen
import kotlinx.serialization.Serializable


/**
 * Serializable data object representing the Profile.
 */
@Serializable
data class Categories(val categoryName: String)

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToCategoriesScreen(
    categoryName: String,
    navOptions: NavOptions? = null) {
    navigate(Categories(categoryName = categoryName), navOptions)
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
            categoryName = categories.categoryName,
            onBackClick = onBackClick,
        )
    }
}