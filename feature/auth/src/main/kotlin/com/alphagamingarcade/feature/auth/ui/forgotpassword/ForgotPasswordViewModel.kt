package com.alphagamingarcade.feature.auth.ui.forgotpassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.data.repository.AuthRepository
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private  val  authRepository: AuthRepository
) : ViewModel() {
    private val _forgotPasswordUiState = MutableStateFlow(UiState(ForgotPasswordData()))
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    fun updateEmail(email: String) {
        _forgotPasswordUiState.updateState {
            copy(
                email = TextFieldData(
                    value = email,
                    errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                ),
            )
        }
    }

    fun sendResetLink() {
        _forgotPasswordUiState.updateWith {
            val result = authRepository.sendResetLink(email.value)

            result.onSuccess {
                _forgotPasswordUiState.update { current ->
                    current.copy(
                        data = current.data.copy(isResetLinkSent = true)
                    )
                }
            }
        }
    }
}

/**
 * Data for [ForgotPasswordScreen].
 *
 * @param email [TextFieldData].
 */
@Immutable
data class ForgotPasswordData(
    val email: TextFieldData = TextFieldData(String()),
    val isResetLinkSent: Boolean = false,
    val canResendEmail: Boolean = true,       // ← add
    val resendCooldownSeconds: Int = 0,       // ← add
)
