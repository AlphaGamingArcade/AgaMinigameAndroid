package com.alphagamingarcade.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.compose.ui.AgamgAppState
import com.alphagamingarcade.feature.auth.navigation.AuthNavGraph
import com.alphagamingarcade.feature.auth.navigation.authNavGraph
import com.alphagamingarcade.feature.auth.navigation.checkYourEmailScreen
import com.alphagamingarcade.feature.auth.navigation.forgotPasswordScreen
import com.alphagamingarcade.feature.auth.navigation.navigateToCheckYourEmailScreen
import com.alphagamingarcade.feature.auth.navigation.navigateToForgotPasswordScreen
import com.alphagamingarcade.feature.auth.navigation.navigateToSignInScreen
import com.alphagamingarcade.feature.auth.navigation.navigateToSignUpScreen
import com.alphagamingarcade.feature.auth.navigation.signInScreen
import com.alphagamingarcade.feature.auth.navigation.signUpScreen
import com.alphagamingarcade.feature.games.navigation.Games
import com.alphagamingarcade.feature.games.navigation.gamesScreen
import com.alphagamingarcade.feature.browse.navigation.browseScreen
import com.alphagamingarcade.feature.favorite.navigation.favoriteScreen
import com.alphagamingarcade.feature.games.navigation.categoriesScreen
import com.alphagamingarcade.feature.games.navigation.navigateToCategoriesScreen
import com.alphagamingarcade.feature.user.navigation.userScreen

/**
 * Composable function that sets up the navigation host for the Jetpack Compose application.
 *
 * @param appState The state of the Jetpack application, containing the navigation controller and user login status.
 * @param onShowSnackbar A lambda function to show a snackbar with a message and an action.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun AgamgNavHost(
    appState: AgamgAppState,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
//    val startDestination =
//        if (appState.isUserLoggedIn) HomeNavGraph::class else AuthNavGraph::class

    val startDestination = Games::class

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(
            nestedNavGraphs = {
                signInScreen(
                    onSignInClick = navController::navigateToSignUpScreen,
                    onShowSnackbar = onShowSnackbar,
                    onForgotPasswordClick = navController::navigateToForgotPasswordScreen
                )
                signUpScreen(
                    onSignInClick = navController::navigateToSignInScreen,
                    onShowSnackbar = onShowSnackbar,
                )
                forgotPasswordScreen(
                    onSignInClick = navController::navigateToSignInScreen,
                    onShowSnackbar = onShowSnackbar,
                    onSendResetEmailClick = { email ->
                        navController.navigateToCheckYourEmailScreen(email = email)
                    }
                )
                checkYourEmailScreen(
                    onBackToSignInClick = navController::navigateToSignInScreen,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
        gamesScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = {},
            onCategoryClick = { categoryId ->
                navController.navigateToCategoriesScreen(categoryId = categoryId)
            },
        )
        categoriesScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = {},
            onBackClick = { navController.popBackStack() },
        )
        browseScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = {}
        )
        favoriteScreen(
            onShowSnackbar = onShowSnackbar,
            onJetpackClick = {}
        )
        userScreen(
            onShowSnackbar = onShowSnackbar,
//            onJetpackClick = {}
        )
    }
}
