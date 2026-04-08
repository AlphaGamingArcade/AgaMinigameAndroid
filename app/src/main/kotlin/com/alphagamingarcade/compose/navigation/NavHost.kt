package com.alphagamingarcade.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.compose.ui.AgamgAppState
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
import com.alphagamingarcade.feature.gamedetail.navigation.GameDetail
import com.alphagamingarcade.feature.gamedetail.navigation.gameDetailScreen
import com.alphagamingarcade.feature.gamedetail.navigation.navigateToGameDetailScreen
import com.alphagamingarcade.feature.games.navigation.categoriesScreen
import com.alphagamingarcade.feature.games.navigation.navigateToCategoriesScreen
import com.alphagamingarcade.feature.user.navigation.accountNavGraph
import com.alphagamingarcade.feature.user.navigation.changePasswordScreen
import com.alphagamingarcade.feature.user.navigation.editProfileScreen
import com.alphagamingarcade.feature.user.navigation.navigateToChangePasswordScreen
import com.alphagamingarcade.feature.user.navigation.navigateToEditProfileScreen
import com.alphagamingarcade.feature.user.navigation.navigateToTransactionScreen
import com.alphagamingarcade.feature.user.navigation.transactionScreen
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

    val startDestination = GameDetail(gameId = "test-id")

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
        gameDetailScreen(
            onShowSnackbar = onShowSnackbar,
            onBackClick = { navController.popBackStack() },
            onPlayClick = { }
        )
        gamesScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = { gameId ->
                navController.navigateToGameDetailScreen(gameId = gameId)
            },
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
        accountNavGraph(
            nestedNavGraphs = {
                userScreen(
                    onShowSnackbar = onShowSnackbar,
                    onEditProfileClick = navController::navigateToEditProfileScreen,
                    onChangePasswordClick = navController::navigateToChangePasswordScreen,
                    onTermsAndPrivacyClick = {},
                    onContactSupportClick = {},
                    onTransactionClick = navController::navigateToTransactionScreen
                )
                changePasswordScreen(
                    onBackClick = { navController.popBackStack() },
                    onShowSnackbar = onShowSnackbar
                )
                editProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onShowSnackbar = onShowSnackbar
                )
                transactionScreen(
                    onBackClick = { navController.popBackStack()},
                    onShowSnackbar = onShowSnackbar
                )
            }
        )
    }
}
