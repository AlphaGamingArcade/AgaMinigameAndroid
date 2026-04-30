package com.alphagamingarcade.feature.user.ui.changepassword

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackPasswordField
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.user.R

/**
 * Change password screen.
 *
 * @param onNavigateBack Navigate back.
 * @param onShowSnackbar Show Snackbar.
 * @param viewModel [ChangePasswordViewModel].
 */
@Composable
internal fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onPopBackToStack: () -> Unit
) {
    val changePasswordState by viewModel.changePasswordUiState.collectAsStateWithLifecycle()

    val successMessage = stringResource(R.string.password_change_success);
    LaunchedEffect(Unit) {
        viewModel.successEvent.collect {
            onShowSnackbar(successMessage, SnackbarAction.NONE, null)
            onPopBackToStack()
        }
    }

    StatefulComposable(
        state = changePasswordState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        ChangePasswordScreen(
            screenData = screenData,
            onNavigateBack = onNavigateBack,
            onCurrentPasswordChange = viewModel::updateCurrentPassword,
            onNewPasswordChange = viewModel::updateNewPassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onChangePasswordClick = viewModel::changePassword,
        )
    }
}

/**
 * Change password screen.
 *
 * @param screenData [ChangePasswordScreenData].
 * @param onNavigateBack Navigate back.
 * @param onCurrentPasswordChange Callback when current password is changed.
 * @param onNewPasswordChange Callback when new password is changed.
 * @param onConfirmPasswordChange Callback when confirm password is changed.
 * @param onChangePasswordClick Callback when change password is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangePasswordScreen(
    screenData: ChangePasswordScreenData,
    onNavigateBack: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // ── Top App Bar ──────────────────────────────────────────────────
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.change_password),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = stringResource(R.string.update_password),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.update_password_sub_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )

                Spacer(modifier = Modifier.height(32.dp))

                JetpackPasswordField(
                    value = screenData.currentPassword.value,
                    errorMessage = screenData.currentPassword.errorMessage,
                    onValueChange = onCurrentPasswordChange,
                    label = { Text(stringResource(R.string.current_password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.current_password),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                JetpackPasswordField(
                    value = screenData.newPassword.value,
                    errorMessage = screenData.newPassword.errorMessage,
                    onValueChange = onNewPasswordChange,
                    label = { Text(stringResource(R.string.new_password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = stringResource(R.string.new_password),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                JetpackPasswordField(
                    value = screenData.confirmPassword.value,
                    errorMessage = screenData.confirmPassword.errorMessage,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text(stringResource(R.string.confirm_new_password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = stringResource(R.string.confirm_new_password),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))

                JetpackButton(
                    onClick = {
                        focusManager.clearFocus()
                        onChangePasswordClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(56.dp),
                    text = { Text(stringResource(R.string.change_password)) },
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}