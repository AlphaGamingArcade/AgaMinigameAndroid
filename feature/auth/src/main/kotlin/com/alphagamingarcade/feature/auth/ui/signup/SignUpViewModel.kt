package com.alphagamingarcade.feature.auth.ui.signup

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWithAppResult
import com.alphagamingarcade.feature.auth.ui.signin.SignInEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


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
                email = TextFieldData(
                    value = email,
                    errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                )
            )
        }
    }

    fun updatePassword(password: String) {
        _signUpUiState.updateState {
            copy(
                password = TextFieldData(
                    value = password,
                    errorMessage = when {
                        password.length < 8 -> "Password must be at least 8 characters"
                        else -> null
                    }
                )
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _signUpUiState.updateState {
            val currentPassword = signUpUiState.value.data.password.value
            copy(
                confirmPassword = TextFieldData(
                    value = confirmPassword,
                    errorMessage = if (confirmPassword == currentPassword) null
                    else "Passwords do not match"
                )
            )
        }
    }

    fun registerWithEmailAndPassword() {
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
                        ?.message

                    val passwordError = result.errors
                        ?.firstOrNull { it.field.equals("Password", ignoreCase = true) }
                        ?.message

                    val confirmPasswordError = result.errors
                        ?.firstOrNull { it.field.equals("ConfirmPassword", ignoreCase = true) }
                        ?.message

                    _signUpUiState.updateState {
                        copy(
                            email = TextFieldData(
                                value = email.value,
                                errorMessage = emailError,
                            ),
                            password = TextFieldData(
                                value = "",
                                errorMessage = passwordError,
                            ),
                            confirmPassword = TextFieldData(
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