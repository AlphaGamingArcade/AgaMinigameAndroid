package com.alphagamingarcade.feature.auth.ui.setupprofile

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.alphagamingarcade.core.ui.components.JetpackTextField
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.feature.auth.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun SetupProfileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    setupProfileViewModel: SetupProfileViewModel = hiltViewModel(),
    onProfileSetupComplete: () -> Unit
) {
    val setupProfileState by setupProfileViewModel.setUpProfileUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        setupProfileViewModel.events.collect { event ->
            when (event) {
                is SetupProfileEvent.OnProfileSetupComplete -> onProfileSetupComplete()
            }
        }
    }

    StatefulComposable(
        state = setupProfileState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        SetupProfileContent(
            screenData = screenData,
            onAccountChange = setupProfileViewModel::updateAccount,
            onNicknameChange = setupProfileViewModel::updateNickname,
            onDobChange = setupProfileViewModel::updateDob,
            onSetupProfile = setupProfileViewModel::onSetupProfile,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetupProfileContent(
    screenData: SetupProfileScreenData,
    onAccountChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onSetupProfile: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var showDatePicker by remember { mutableStateOf(false) }
    val maxDateMillis = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= maxDateMillis
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis

                        if (selectedDateMillis != null) {
                            val formattedDate = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(Date(selectedDateMillis))

                            onDobChange(formattedDate)
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

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
            text = stringResource(R.string.setup_profile_title),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )

        Spacer(modifier = Modifier.height(24.dp))

        JetpackTextField(
            value = screenData.account.value,
            errorMessage = screenData.account.errorMessage,
            onValueChange = onAccountChange,
            label = { Text(stringResource(R.string.account)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.account),
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        JetpackTextField(
            value = screenData.nickname.value,
            errorMessage = screenData.nickname.errorMessage,
            onValueChange = onNicknameChange,
            label = { Text(stringResource(R.string.nickname)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = stringResource(R.string.nickname),
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            JetpackTextField(
                value = screenData.dob.value,
                errorMessage = screenData.dob.errorMessage,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.date_of_birth)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = stringResource(R.string.date_of_birth),
                    )
                },
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable (
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        focusManager.clearFocus()
                        showDatePicker = true

                    }
            )
        }

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
            text = {
                Text(stringResource(R.string.continue_btn))
            },
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}