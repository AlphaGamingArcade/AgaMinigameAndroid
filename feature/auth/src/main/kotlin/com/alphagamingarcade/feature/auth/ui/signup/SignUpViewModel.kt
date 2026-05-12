package com.alphagamingarcade.feature.auth.ui.signup

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.extensions.isPasswordValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.UiText
import com.alphagamingarcade.core.ui.utils.toUiText
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWithAppResult
import com.alphagamingarcade.feature.auth.ui.signin.SignInEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import com.alphagamingarcade.feature.auth.R


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _signUpUiState = MutableStateFlow(UiState(SignUpScreenData()))
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _events = kotlinx.coroutines.channels.Channel<SignInEvent>()
    val events = _events.receiveAsFlow()

    fun updateEmail(email: String) {
        _signUpUiState.updateState {
            copy(
                email = this.email.copy(
                    value = email,
                    errorMessage = when {
                        email.isBlank() -> null
                        !email.isEmailValid() -> UiText.StringResource(R.string.email_not_valid)
                        else -> null
                    },
                )
            )
        }
    }
    fun updatePassword(password: String) {
        _signUpUiState.updateState {
            copy(
                password = this.password.copy(
                    value = password,
                    errorMessage = when {
                        password.isBlank() -> null
                        !password.isPasswordValid() -> UiText.StringResource(R.string.password_not_valid)
                        else -> null
                    },
                )
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _signUpUiState.updateState {
            val currentPassword = signUpUiState.value.data.password.value

            copy(
                confirmPassword = this.confirmPassword.copy(
                    value = confirmPassword,
                    errorMessage = when {
                        confirmPassword.isBlank() -> null
                        confirmPassword != currentPassword ->
                            UiText.StringResource(R.string.passwords_do_not_match)
                        else -> null
                    },
                )
            )
        }
    }

    fun registerWithEmailAndPassword() {
        if (!validate()) return

        _signUpUiState.updateWithAppResult {
            val result = authRepository.registerWithEmailAndPassword(
                email = email.value,
                password = password.value,
                confirmPassword = confirmPassword.value
            )

            when (result) {
                is AppResult.Success -> {
                    val isEmailVerified = authRepository.isEmailVerified().first()
                    val hasProfileSetup = authRepository.hasProfileSetup().first()

                    when {
                        !isEmailVerified -> {
                            _events.send(SignInEvent.NavigateToVerifyEmail(email.value))
                        }
                        !hasProfileSetup -> {
                            _events.send(SignInEvent.NavigateToProfileSetup)
                        }
                        else -> {
                            _events.send(SignInEvent.NavigateToPrevious)
                        }
                    }
                }

                is AppResult.Error -> {
                    val emailError = result.errors
                        ?.firstOrNull { it.field.equals("Email", ignoreCase = true) }
                        ?.toUiText()

                    val passwordError = result.errors
                        ?.firstOrNull { it.field.equals("Password", ignoreCase = true) }
                        ?.toUiText()

                    val confirmPasswordError = result.errors
                        ?.firstOrNull {
                            it.field.equals("ConfirmPassword", ignoreCase = true) ||
                                    it.field.equals("confirmPassword", ignoreCase = true) ||
                                    it.field.equals("confirm_password", ignoreCase = true)
                        }
                        ?.toUiText()

                    _signUpUiState.updateState {
                        copy(
                            email = email.copy(errorMessage = emailError),
                            password = password.copy(
                                value = "",
                                errorMessage = passwordError,
                            ),
                            confirmPassword = confirmPassword.copy(
                                value = "",
                                errorMessage = confirmPasswordError,
                            ),
                        )
                    }
                }
            }

            result
        }
    }

    private fun validate(): Boolean {
        val data = _signUpUiState.value.data

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

        val confirmPasswordError = when {
            data.confirmPassword.value.isBlank() -> UiText.StringResource(R.string.confirm_password_required)
            data.confirmPassword.value != data.password.value ->
                UiText.StringResource(R.string.passwords_do_not_match)
            else -> null
        }

        val isValid = listOf(emailError, passwordError, confirmPasswordError)
            .all { it == null }

        if (!isValid) {
            _signUpUiState.updateState {
                copy(
                    email = email.copy(errorMessage = emailError),
                    password = password.copy(errorMessage = passwordError),
                    confirmPassword = confirmPassword.copy(errorMessage = confirmPasswordError),
                )
            }
        }

        return isValid
    }
}


/**
 * Data for [SignUpScreen].
 *
 * @param email [TextFieldData].
 * @param password [TextFieldData].
 * @param confirmPassword [TextFieldData].
 */
@Immutable
data class SignUpScreenData(
    val email: TextFieldData = TextFieldData(String()),
    val password: TextFieldData = TextFieldData(String()),
    val confirmPassword: TextFieldData = TextFieldData(String())
)