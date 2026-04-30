package com.alphagamingarcade.feature.user.ui.editprofile

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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
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
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.user.R

/**
 * Edit profile screen.
 *
 * @param onNavigateBack Navigate back.
 * @param onShowSnackbar Show Snackbar.
 * @param viewModel [EditProfileViewModel].
 */
@Composable
internal fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val editProfileState by viewModel.editProfileUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.successEvent.collect {
            onShowSnackbar("Nickname updated successfully!", SnackbarAction.NONE, null)
        }
    }

    StatefulComposable(
        state = editProfileState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        EditProfileScreen(
            screenData = screenData,
            onNavigateBack = onNavigateBack,
            onNicknameChange = viewModel::updateNickname,
            onSaveClick = viewModel::saveProfile,
        )
    }
}

/**
 * Edit profile screen content.
 *
 * @param screenData [EditProfileScreenData].
 * @param onNavigateBack Navigate back.
 * @param onSaveClick Callback when save is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    screenData: EditProfileScreenData,
    onNavigateBack: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
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
                        text = stringResource(R.string.edit_profile),
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
                    text = stringResource(R.string.update_profile),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.update_profile_sub_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )

                Spacer(modifier = Modifier.height(32.dp))

                JetpackTextField(
                    value = screenData.nickname.value,
                    onValueChange = onNicknameChange,
                    label = { Text(stringResource(R.string.nickname)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.nickname),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )



                JetpackButton(
                    onClick = {
                        focusManager.clearFocus()
                        onSaveClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(56.dp),
                    text = { Text(stringResource(R.string.save_changes)) },
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}