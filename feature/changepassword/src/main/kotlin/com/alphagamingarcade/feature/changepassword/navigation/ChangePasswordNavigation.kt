package com.alphagamingarcade.feature.changepassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.changepassword.ui.ChangePasswordScreen
import kotlinx.serialization.Serializable

/**
 * Serializable data object representing the Profile.
 */
@Serializable
data object ChangePassword

fun NavController.navigateToChangePasswordScreen(
    navOptions: NavOptions?
){
    navigate(ChangePassword, navOptions)
}

fun NavGraphBuilder.changePasswordScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onGameClick: (String) -> Unit,
){
    composable<ChangePassword> {
        ChangePasswordScreen(
            onShowSnackbar = onShowSnackbar,
            onGameClick = onGameClick
        )
    }
}