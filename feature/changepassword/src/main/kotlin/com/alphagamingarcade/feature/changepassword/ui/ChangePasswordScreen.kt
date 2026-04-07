package com.alphagamingarcade.feature.changepassword.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.model.data.Game

@Composable
internal fun ChangePasswordScreen(
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel(),
    onGameClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
){
    val browseState by changePasswordViewModel.browseUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = browseState,
        onShowSnackbar = onShowSnackbar,
    ) { browseScreenData ->
        ChangePasswordScreen(
            games = browseScreenData.games,
            onGameClick = onGameClick
        )
    }

}

@Composable
private fun ChangePasswordScreen(
    games: List<Game>,
    onGameClick: (String) -> Unit
) {
    Column{
        Text("Hello Chnage password Screen")
    }
}