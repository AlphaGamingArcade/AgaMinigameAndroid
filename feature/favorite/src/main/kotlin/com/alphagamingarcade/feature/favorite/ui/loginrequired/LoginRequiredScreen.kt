package com.alphagamingarcade.feature.favorite.ui.loginrequired

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.components.LoginRequired
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable

@Composable
internal fun LoginRequiredScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    loginRequiredViewModel: LoginRequiredViewModel = hiltViewModel(),
) {
    val loginRequiredState by loginRequiredViewModel.loginRequiredUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = loginRequiredState,
        onShowSnackbar = onShowSnackbar,
    ) { _ ->
        LoginRequiredScreen(
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
        )
    }
}

@Composable
private fun LoginRequiredScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LoginRequired(
            modifier = Modifier.fillMaxSize(),
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick
        )
    }
}