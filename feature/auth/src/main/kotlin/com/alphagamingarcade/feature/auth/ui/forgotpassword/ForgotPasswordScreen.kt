package com.alphagamingarcade.feature.auth.ui.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackTextButton
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.auth.R

@Composable
internal fun ForgotPasswordScreen(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(),
    onSendResetEmailClick: (email: String) -> Unit
) {
    val forgotPasswordState by forgotPasswordViewModel.signInUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = forgotPasswordState,
        onShowSnackbar = onShowSnackbar,
    ) { forgotPasswordData ->
        ForgotPasswordScreen(
            screenData = forgotPasswordData,
            onEmailChange = forgotPasswordViewModel::updateEmail,
            onSignInClick = onSignInClick,
            onSendResetEmailClick
        )
    }
}

@Composable
private fun ForgotPasswordScreen(
    screenData: ForgotPasswordData,
    onEmailChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSendResetEmailClick: (email: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(
            text = stringResource(R.string.forgot_password),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.forgot_password_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        JetpackTextField(
            value = screenData.email.value,
            errorMessage = screenData.email.errorMessage,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackButton(
            onClick = {
                focusManager.clearFocus()
                 onSendResetEmailClick(screenData.email.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            text = { Text(stringResource(R.string.send_reset_link)) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.remember_your_password))
            JetpackTextButton(onClick = onSignInClick) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
@PreviewThemes
@PreviewDevices
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        screenData = ForgotPasswordData(),
        onEmailChange = {},
        onSignInClick = {},
        onSendResetEmailClick = {}
    )
}