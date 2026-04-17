package com.alphagamingarcade.feature.auth.ui.signin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.extensions.isPasswordValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWith
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
                email = TextFieldData(
                    value = email,
                    errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                ),
            )
        }
    }

    fun updatePassword(password: String) {
        _signInUiState.updateState {
            copy(
                password = TextFieldData(
                    value = password,
                    errorMessage = if (password.isPasswordValid()) null else "Password Not Valid",
                ),
            )
        }
    }

    fun loginWithEmailAndPassword() {
        _signInUiState.updateWith {
            val result = authRepository.signInWithEmailAndPassword(
                email = email.value,
                password = password.value,
            )

            if (result.isSuccess) {
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

            result
        }
    }
}

sealed class SignInEvent {
    data class NavigateToVerifyEmail(val email: String) : SignInEvent()
    object NavigateToProfileSetup : SignInEvent()
    object NavigateToPrevious : SignInEvent()
}

/**
 * Data for [SignInScreen].
 *
 * @param email [TextFieldData].
 * @param password [TextFieldData].
 */
@Immutable
data class SignInScreenData(
    val email: TextFieldData = TextFieldData(String()),
    val password: TextFieldData = TextFieldData(String()),
)
