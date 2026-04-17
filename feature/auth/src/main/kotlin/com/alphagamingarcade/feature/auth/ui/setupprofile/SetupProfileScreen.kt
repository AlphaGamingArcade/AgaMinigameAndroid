package com.alphagamingarcade.feature.auth.ui.setupprofile

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.components.JetpackButton
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable

@Composable
internal fun SetupProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    setupProfileViewModel: SetupProfileViewModel = hiltViewModel(),
) {
    val setupProfileState by setupProfileViewModel.setUpProfileUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = setupProfileState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        SetupProfileScreen(
            screenData = screenData,
            onAccountChange = setupProfileViewModel::updateAccount,
            onNicknameChange = setupProfileViewModel::updateNickname,
            onDobChange = setupProfileViewModel::updateDob,
            onSetupProfile = setupProfileViewModel::onSetupProfile,
        )
    }
}

@Composable
private fun SetupProfileScreen(
    screenData: SetupProfileScreenData,
    onAccountChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onSetupProfile: () -> Unit,
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
            text = "Set Up Profile",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tell us a bit about yourself to get started.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        JetpackTextField(
            value = screenData.account.value,
            errorMessage = screenData.account.errorMessage,
            onValueChange = onAccountChange,
            label = { Text("Account") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Account",
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackTextField(
            value = screenData.nickname.value,
            errorMessage = screenData.nickname.errorMessage,
            onValueChange = onNicknameChange,
            label = { Text("Nickname") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "Nickname",
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackTextField(
            value = screenData.dob.value,
            errorMessage = screenData.dob.errorMessage,
            onValueChange = onDobChange,
            label = { Text("Date of Birth (yyyy-MM-dd)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Date of Birth",
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        JetpackButton(
            onClick = {
                focusManager.clearFocus()
                onSetupProfile()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            text = { Text("Continue") },
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}