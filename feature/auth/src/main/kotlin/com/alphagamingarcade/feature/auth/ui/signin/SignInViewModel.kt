package com.alphagamingarcade.feature.auth.ui.signin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.data.repository.AuthRepository
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.extensions.isPasswordValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.UiText
import com.alphagamingarcade.core.ui.utils.toUiText
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWithAppResult
import com.alphagamingarcade.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _signInUiState = MutableStateFlow(UiState(SignInScreenData()))
    val signInUiState = _signInUiState.asStateFlow()

    private val _events = kotlinx.coroutines.channels.Channel<SignInEvent>()
    val events = _events.receiveAsFlow()

    fun updateEmail(email: String) {
        _signInUiState.updateState {
            copy(
                email = this.email.copy(
                    value = email,
                    errorMessage = when {
                        email.isBlank() -> null
                        !email.isEmailValid() -> UiText.StringResource(R.string.email_not_valid)
                        else -> null
                    },
                ),
            )
        }
    }

    fun updatePassword(password: String) {
        _signInUiState.updateState {
            copy(
                password = this.password.copy(
                    value = password,
                    errorMessage = when {
                        password.isBlank() -> null
                        !password.isPasswordValid() -> UiText.StringResource(R.string.password_not_valid)
                        else -> null
                    },
                ),
            )
        }
    }

    fun loginWithEmailAndPassword() {
        if (!validate()) return

        _signInUiState.updateWithAppResult {
            val result = authRepository.signInWithEmailAndPassword(
                email = email.value,
                password = password.value,
            )

            when (result) {
                is AppResult.Success -> {
                    val isEmailVerified = authRepository.isEmailVerified().first()
                    val hasProfileSetup = authRepository.hasProfileSetup().first()

                    when {
                        !isEmailVerified -> _events.send(SignInEvent.NavigateToVerifyEmail(email.value))
                        !hasProfileSetup -> _events.send(SignInEvent.NavigateToProfileSetup)
                        else -> _events.send(SignInEvent.NavigateToPrevious)
                    }
                }

                is AppResult.Error -> {
                    val emailError = result.errors
                        ?.firstOrNull { it.field.equals("Email", ignoreCase = true) }
                        ?.toUiText()

                    val passwordError = result.errors
                        ?.firstOrNull { it.field.equals("Password", ignoreCase = true) }
                        ?.toUiText()

                    _signInUiState.updateState {
                        copy(
                            email = email.copy(errorMessage = emailError),
                            password = password.copy(
                                value = "",
                                errorMessage = passwordError,
                            ),
                        )
                    }
                }
            }

            result
        }
    }

    private fun validate(): Boolean {
        val data = _signInUiState.value.data

        val emailError = when {
            data.email.value.isBlank() -> UiText.StringResource(R.string.email_required)
            !data.email.value.isEmailValid() -> UiText.StringResource(R.string.email_not_valid)
            else -> null
        }

        val passwordError = when {
            data.password.value.isBlank() -> UiText.StringResource(R.string.password_required)
            !data.password.value.isPasswordValid() -> UiText.StringResource(R.string.password_not_valid)
            else -> null
        }

        val isValid = listOf(emailError, passwordError).all { it == null }

        if (!isValid) {
            _signInUiState.updateState {
                copy(
                    email = email.copy(errorMessage = emailError),
                    password = password.copy(errorMessage = passwordError),
                )
            }
        }

        return isValid
    }
}

sealed class SignInEvent {
    data class NavigateToVerifyEmail(val email: String) : SignInEvent()
    object NavigateToProfileSetup : SignInEvent()
    object NavigateToPrevious : SignInEvent()
}

@Immutable
data class SignInScreenData(
    val email: TextFieldData = TextFieldData(String()),
    val password: TextFieldData = TextFieldData(String()),
)