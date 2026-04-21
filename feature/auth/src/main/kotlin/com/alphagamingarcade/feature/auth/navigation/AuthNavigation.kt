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
import com.alphagamingarcade.feature.auth.ui.resetlinksent.ResetLinkSentScreen
import com.alphagamingarcade.feature.auth.ui.setupprofile.SetupProfileScreen
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
 * Forgot Password route.
 */
@Serializable
data class ResetLinkSent(val email: String)


/**
 * Forgot Password route.
 */
@Serializable
data object SetupProfile

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
 * Navigate to the auth navigation graph.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToResetLinkSentScreen(email: String, navOptions: NavOptions? = null) {
    navigate(ResetLinkSent(email), navOptions)
}


/**
 * Navigate to the forgot password route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateSetupProfileScreen(navOptions: NavOptions? = null) {
    navigate(SetupProfile, navOptions)
}



/**
 * Sign in screen.
 *
 * @param onSignUpLinkClick Callback when sign up is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signInScreen(
    onSignUpLinkClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onProfileSetup: () -> Unit,
    onCheckYourEmail: (String) -> Unit,
    onPrevious: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignIn> {
        SignInScreen(
            onSignUpLinkClick = onSignUpLinkClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onShowSnackbar = onShowSnackbar,
            onProfileSetup = onProfileSetup,
            onCheckYourEmail = onCheckYourEmail,
            onPrevious = onPrevious
        )
    }
}

/**
 * Sign up screen.
 *
 * @param onSignInLinkClick Callback when sign in is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signUpScreen(
    onSignInLinkClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onProfileSetup: () -> Unit,
    onCheckYourEmail: (String) -> Unit,
    onPrevious: () -> Unit,
) {
    composable<SignUp> {
        SignUpScreen(
            onSignInLinkClick = onSignInLinkClick,
            onShowSnackbar = onShowSnackbar,
            onProfileSetup = onProfileSetup,
            onCheckYourEmail = onCheckYourEmail,
            onPrevious = onPrevious
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
    onResetLinkSent: (String) -> Unit,
) {
    composable<ForgotPassword> {
        ForgotPasswordScreen(
            onSignInClick = onSignInClick,
            onShowSnackbar = onShowSnackbar,
            onResetLinkSent = onResetLinkSent
        )
    }
}

/**
 * Forgot password screen.
 *
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.resetLinkSentScreen(
    onBackToSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,

) {
    composable<ResetLinkSent> { backStackEntry ->
        val email = backStackEntry.toRoute<ResetLinkSent>();
        ResetLinkSentScreen(
            email = email.email,
            onBackToSignInClick = onBackToSignInClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}


fun NavGraphBuilder.setupProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onProfileSetupComplete: () -> Unit
) {
    composable<SetupProfile> {
        SetupProfileScreen(
            onShowSnackbar = onShowSnackbar,
            onProfileSetupComplete = onProfileSetupComplete
        )
    }
}


fun NavController.navigateToCheckYourEmailScreen(
    email: String,
    navOptions: NavOptions? = null,
) {
    navigate(CheckYourEmail(email), navOptions)
}


fun NavGraphBuilder.checkYourEmailScreen(
    onBackToSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<CheckYourEmail> { backStackEntry ->
        val route = backStackEntry.toRoute<CheckYourEmail>()
        CheckYourEmailScreen(
            email = route.email,
            onBackToSignInClick = onBackToSignInClick,
            onShowSnackbar = onShowSnackbar
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
