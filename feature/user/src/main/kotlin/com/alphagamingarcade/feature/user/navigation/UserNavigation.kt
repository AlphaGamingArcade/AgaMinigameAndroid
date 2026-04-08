package com.alphagamingarcade.feature.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.user.ui.changepassword.ChangePasswordScreen
import com.alphagamingarcade.feature.user.ui.editprofile.EditProfileScreen
import com.alphagamingarcade.feature.user.ui.transaction.TransactionScreen
import com.alphagamingarcade.feature.user.ui.user.UserScreen
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
fun NavGraphBuilder.userScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onTermsAndPrivacyClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onTransactionClick: () -> Unit
) {
    composable<User> {
        UserScreen(
            onShowSnackbar = onShowSnackbar,
            onEditProfileClick = onEditProfileClick,
            onChangePasswordClick = onChangePasswordClick,
            onTermsAndPrivacyClick = onTermsAndPrivacyClick,
            onContactSupportClick = onContactSupportClick,
            onTransactionClick = onTransactionClick
        )
    }
}

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 */
fun NavGraphBuilder.changePasswordScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<ChangePassword> {
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
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<Transaction> {
        TransactionScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
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
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onBackClick: () -> Unit
) {
    composable<EditProfile> {
        EditProfileScreen(
            onNavigateBack = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
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
