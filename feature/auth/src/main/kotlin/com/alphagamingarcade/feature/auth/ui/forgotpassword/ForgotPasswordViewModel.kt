package com.alphagamingarcade.feature.auth.ui.forgotpassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.feature.auth.ui.signin.SignInScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor() : ViewModel() {
    private val _signInUiState = MutableStateFlow(UiState(ForgotPasswordData()))
    val signInUiState = _signInUiState.asStateFlow()

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
}

/**
 * Data for [ForgotPasswordScreen].
 *
 * @param email [TextFieldData].
 * @param password [TextFieldData].
 */
@Immutable
data class ForgotPasswordData(
    val email: TextFieldData = TextFieldData(String()),
    val password: TextFieldData = TextFieldData(String()),
)
