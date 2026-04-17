package com.alphagamingarcade.feature.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.user.ui.changepassword.ChangePasswordScreen
import com.alphagamingarcade.feature.user.ui.editprofile.EditProfileScreen
import com.alphagamingarcade.feature.user.ui.loginrequired.LoginRequiredScreen
import com.alphagamingarcade.feature.user.ui.transaction.TransactionScreen
import com.alphagamingarcade.feature.user.ui.profile.ProfileScreen
import kotlinx.serialization.Serializable

/**
 * Account navigation graph.
 */
@Serializable
data object AccountNavGraph

/**
 * User route.
 */
@Serializable
data object User

/**
 * Sign up route.
 */
@Serializable
data object ChangePassword


@Serializable
data object EditProfile


@Serializable
data object Transaction


/**
 * Navigate to the auth navigation graph.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToAccountNavGraph(navOptions: NavOptions? = null) {
    navigate(AccountNavGraph, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToUserScreen(navOptions: NavOptions? = null) {
    navigate(User, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToChangePasswordScreen(navOptions: NavOptions? = null) {
    navigate(ChangePassword, navOptions)
}


///**
// * Adds the Profile screen to the navigation graph.
// *
// * @param onShowSnackbar Lambda function to show a snackbar message.
// */
fun NavGraphBuilder.profileScreen(
    isLoggedIn: Boolean,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onTermsAndPrivacyClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onTransactionClick: () -> Unit
) {
    composable<User> {
        if (!isLoggedIn){
            LoginRequiredScreen(
                onShowSnackbar = onShowSnackbar,
                onLoginClick = onSignInClick,
                onSignUpClick = onSignUpClick
            )
        } else {
            ProfileScreen(
                onShowSnackbar = onShowSnackbar,
                onEditProfileClick = onEditProfileClick,
                onChangePasswordClick = onChangePasswordClick,
                onTermsAndPrivacyClick = onTermsAndPrivacyClick,
                onContactSupportClick = onContactSupportClick,
                onTransactionClick = onTransactionClick,
            )
        }
    }
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.changePasswordScreen(
    isLoggedIn: Boolean,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<ChangePassword> {
        if (!isLoggedIn){
            LoginRequiredScreen(
                onShowSnackbar = onShowSnackbar,
                onLoginClick = onSignInClick,
                onSignUpClick = onSignUpClick
            )
        }

        ChangePasswordScreen(
            onNavigateBack = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToEditProfileScreen(navOptions: NavOptions? = null) {
    navigate(EditProfile, navOptions)
}


/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.transactionScreen(
    isLoggedIn: Boolean,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<Transaction> {
        if (!isLoggedIn){
            LoginRequiredScreen(
                onShowSnackbar = onShowSnackbar,
                onLoginClick = onSignInClick,
                onSignUpClick = onSignUpClick
            )
        } else {
            TransactionScreen(
                onBackClick = onBackClick,
                onShowSnackbar = onShowSnackbar,
            )
        }

    }
}


/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToTransactionScreen(navOptions: NavOptions? = null) {
    navigate(Transaction, navOptions)
}


/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.editProfileScreen(
    isLoggedIn: Boolean,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<EditProfile> {
        if (!isLoggedIn){
            LoginRequiredScreen(
                onShowSnackbar = onShowSnackbar,
                onLoginClick = onSignInClick,
                onSignUpClick = onSignUpClick
            )
        } else {
        EditProfileScreen(
            onNavigateBack = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
        }
    }
}

/**
 * Auth navigation graph.
 *
 * @param nestedNavGraphs Nested navigation graphs.
 */
fun NavGraphBuilder.accountNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<AccountNavGraph>(startDestination = User) {
        nestedNavGraphs()
    }
}
