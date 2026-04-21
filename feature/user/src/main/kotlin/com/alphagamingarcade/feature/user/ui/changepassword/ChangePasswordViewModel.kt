package com.alphagamingarcade.feature.user.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateWith
import com.alphagamingarcade.core.utils.OneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordScreenData(
    val currentPassword: FieldState = FieldState(),
    val newPassword: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
)

data class FieldState(
    val value: String = "",
    val errorMessage: String? = null,
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
     private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _changePasswordUiState = MutableStateFlow(
        UiState(data = ChangePasswordScreenData())
    )
    val changePasswordUiState: StateFlow<UiState<ChangePasswordScreenData>> =
        _changePasswordUiState.asStateFlow()

    private val _successEvent = Channel<Unit>(Channel.BUFFERED)
    val successEvent = _successEvent.receiveAsFlow()

    fun updateCurrentPassword(value: String) {
        _changePasswordUiState.update { state ->
            state.copy(
                data = state.data.copy(
                    currentPassword = state.data.currentPassword.copy(
                        value = value,
                        errorMessage = null,
                    ),
                ),
            )
        }
    }

    fun updateNewPassword(value: String) {
        _changePasswordUiState.update { state ->
            state.copy(
                data = state.data.copy(
                    newPassword = state.data.newPassword.copy(
                        value = value,
                        errorMessage = null,
                    ),
                ),
            )
        }
    }

    fun updateConfirmPassword(value: String) {
        _changePasswordUiState.update { state ->
            state.copy(
                data = state.data.copy(
                    confirmPassword = state.data.confirmPassword.copy(
                        value = value,
                        errorMessage = null,
                    ),
                ),
            )
        }
    }

    fun changePassword() {
        if (!validate()) return

        _changePasswordUiState.updateWith {
            profileRepository.changePassword(
                currentPassword = currentPassword.value,
                newPassword = newPassword.value,
                confirmPassword = confirmPassword.value,
            ).onSuccess {
                _successEvent.send(Unit)
                profileRepository.signOut()
            }
        }
    }

    private fun validate(): Boolean {
        val data = _changePasswordUiState.value.data

        val currentPasswordError = when {
            data.currentPassword.value.isBlank() -> "Current password is required"
            else -> null
        }

        val newPasswordError = when {
            data.newPassword.value.isBlank() -> "New password is required"
            data.newPassword.value.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }

        val confirmPasswordError = when {
            data.confirmPassword.value.isBlank() -> "Please confirm your new password"
            data.confirmPassword.value != data.newPassword.value -> "Passwords do not match"
            else -> null
        }

        val isValid = listOf(currentPasswordError, newPasswordError, confirmPasswordError)
            .all { it == null }

        if (!isValid) {
            _changePasswordUiState.update { state ->
                state.copy(
                    data = state.data.copy(
                        currentPassword = state.data.currentPassword.copy(errorMessage = currentPasswordError),
                        newPassword = state.data.newPassword.copy(errorMessage = newPasswordError),
                        confirmPassword = state.data.confirmPassword.copy(errorMessage = confirmPasswordError),
                    ),
                )
            }
        }

        return isValid
    }
}