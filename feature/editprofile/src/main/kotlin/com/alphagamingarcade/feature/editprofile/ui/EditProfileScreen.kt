package com.alphagamingarcade.feature.editprofile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable

@Composable
internal fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
){
    val editProfileState by editProfileViewModel.editProfileUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = editProfileState,
        onShowSnackbar = onShowSnackbar
    ) {
        EditProfileScreen()
    }
}

@Composable
private  fun  EditProfileScreen(){
    Column {
        Text("Edit profile")
    }
}