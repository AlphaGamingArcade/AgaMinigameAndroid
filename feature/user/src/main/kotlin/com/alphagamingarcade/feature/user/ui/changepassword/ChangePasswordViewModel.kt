package com.alphagamingarcade.feature.user.ui.changepassword

import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.extensions.isPasswordValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.UiText
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWithAppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.alphagamingarcade.feature.user.R

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

        _changePasswordUiState.updateWithAppResult {
            val result = profileRepository.changePassword(
                currentPassword = currentPassword.value,
                newPassword = newPassword.value,
                confirmPassword = confirmPassword.value,
            )

            when (result) {
                is AppResult.Success -> {
                    _successEvent.send(Unit)
                    profileRepository.signOut()
                }

                is AppResult.Error -> {
                    val currentPasswordError = result.errors
                        ?.firstOrNull { it.field.equals("currentPassword", ignoreCase = true) }
                        ?.message
                        ?.let { UiText.DynamicString(it) }

                    val newPasswordError = result.errors
                        ?.firstOrNull { it.field.equals("newPassword", ignoreCase = true) }
                        ?.message
                        ?.let { UiText.DynamicString(it) }

                    val confirmPasswordError = result.errors
                        ?.firstOrNull {
                            it.field.equals("confirmPassword", ignoreCase = true) ||
                                    it.field.equals("confirm_password", ignoreCase = true)
                        }
                        ?.message
                        ?.let { UiText.DynamicString(it) }

                    _changePasswordUiState.updateState {
                        copy(
                            currentPassword = TextFieldData(
                                value = currentPassword.value,
                                errorMessage = currentPasswordError ?: currentPassword.errorMessage
                            ),
                            newPassword = TextFieldData(
                                value = newPassword.value,
                                errorMessage = newPasswordError ?: newPassword.errorMessage
                            ),
                            confirmPassword = TextFieldData(
                                value = confirmPassword.value,
                                errorMessage = confirmPasswordError ?: confirmPassword.errorMessage
                            )
                        )
                    }
                }
            }

            result
        }
    }

    private fun validate(): Boolean {
        val data = _changePasswordUiState.value.data

        val currentPasswordError = when {
            data.currentPassword.value.isBlank() ->
                UiText.StringResource(R.string.password_required)
            else -> null
        }

        val newPasswordError = when {
            data.newPassword.value.isBlank() ->
                UiText.StringResource(R.string.password_required)
            !data.newPassword.value.isPasswordValid() ->
                UiText.StringResource(R.string.password_min_characters)
            else -> null
        }

        val confirmPasswordError = when {
            data.confirmPassword.value.isBlank() ->
                UiText.StringResource(R.string.confirm_password_required)
            data.confirmPassword.value != data.newPassword.value ->
                UiText.StringResource(R.string.passwords_do_not_match)
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
                    )
                )
            }
        }

        return isValid
    }
}

data class ChangePasswordScreenData(
    val currentPassword: TextFieldData = TextFieldData(String()),
    val newPassword: TextFieldData = TextFieldData(String()),
    val confirmPassword: TextFieldData = TextFieldData(String()),
)
