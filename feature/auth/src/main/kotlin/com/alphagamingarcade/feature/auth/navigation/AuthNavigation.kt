package com.alphagamingarcade.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.auth.ui.checkyouremail.CheckYourEmailScreen
import com.alphagamingarcade.feature.auth.ui.forgotpassword.ForgotPasswordScreen
import com.alphagamingarcade.feature.auth.ui.signin.SignInScreen
import com.alphagamingarcade.feature.auth.ui.signup.SignUpScreen
import kotlinx.serialization.Serializable

/**
 * Auth navigation graph.
 */
@Serializable
data object AuthNavGraph

/**
 * Sign in route.
 */
@Serializable
data object SignIn

/**
 * Sign up route.
 */
@Serializable
data object SignUp

/**
 * Forgot Password route.
 */
@Serializable
data object ForgotPassword

/**
 * Check your Email Route
 */
@Serializable
data class CheckYourEmail(val email: String)

/**
 * Navigate to the auth navigation graph.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToAuthNavGraph(navOptions: NavOptions? = null) {
    navigate(AuthNavGraph, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToSignInScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(SignIn) { navOptions() }
}

/**
 * Navigate to the sign up route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToSignUpScreen(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

/**
 * Navigate to the forgot password route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToForgotPasswordScreen(navOptions: NavOptions? = null) {
    navigate(ForgotPassword, navOptions)
}


/**
 * Sign in screen.
 *
 * @param onSignUpClick Callback when sign up is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signInScreen(
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignIn> {
        SignInScreen(
            onSignUpClick = onSignUpClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Sign up screen.
 *
 * @param onSignInClick Callback when sign in is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signUpScreen(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignUp> {
        SignUpScreen(
            onSignInClick = onSignInClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}


/**
 * Forgot password screen.
 *
 * @param onSignInClick Callback when sign in is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.forgotPasswordScreen(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onSendResetEmailClick: (email: String) -> Unit
) {
    composable<ForgotPassword> {
        ForgotPasswordScreen(
            onSignInClick = onSignInClick,
            onShowSnackbar = onShowSnackbar,
            onSendResetEmailClick = onSendResetEmailClick
        )
    }
}


//  Navigation route
fun NavController.navigateToCheckYourEmailScreen(
    email: String,
    navOptions: NavOptions? = null,
) {
    navigate(CheckYourEmail(email), navOptions)
}

// NavGraphBuilder
fun NavGraphBuilder.checkYourEmailScreen(
    onBackToSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<CheckYourEmail> { backStackEntry ->
        val route = backStackEntry.toRoute<CheckYourEmail>()
        CheckYourEmailScreen(
            email = route.email,
            onBackToSignInClick = onBackToSignInClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Auth navigation graph.
 *
 * @param nestedNavGraphs Nested navigation graphs.
 */
fun NavGraphBuilder.authNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<AuthNavGraph>(startDestination = SignIn) {
        nestedNavGraphs()
    }
}
