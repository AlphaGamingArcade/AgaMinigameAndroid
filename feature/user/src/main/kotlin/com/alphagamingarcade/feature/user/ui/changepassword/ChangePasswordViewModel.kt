package com.alphagamingarcade.feature.user.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    // private val userRepository: UserRepository,
) : ViewModel() {

    private val _changePasswordUiState = MutableStateFlow(
        UiState(data = ChangePasswordScreenData())
    )
    val changePasswordUiState: StateFlow<UiState<ChangePasswordScreenData>> =
        _changePasswordUiState.asStateFlow()

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

        viewModelScope.launch {
            _changePasswordUiState.update { it.copy(loading = true) }
            try {
                // userRepository.changePassword(
                //     currentPassword = _changePasswordUiState.value.data.currentPassword.value,
                //     newPassword = _changePasswordUiState.value.data.newPassword.value,
                // )
                _changePasswordUiState.update {
                    UiState(data = ChangePasswordScreenData()) // reset form on success
                }
            } catch (e: Exception) {
                _changePasswordUiState.update {
                    it.copy(
                        loading = false,
//                        error = OneTimeEvent(e),
                    )
                }
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