package com.alphagamingarcade.feature.editprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import kotlinx.serialization.Serializable

@Serializable
data object EditProfile

fun NavController.navigateToEditProfile(
    navOptions: NavOptions?
){
    navigate(EditProfile, navOptions)
}

fun  NavGraphBuilder.editProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean
){
    composable<EditProfile>{

    }
}