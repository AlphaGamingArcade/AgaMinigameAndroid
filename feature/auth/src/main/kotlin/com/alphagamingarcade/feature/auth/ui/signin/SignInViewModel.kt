package com.alphagamingarcade.feature.auth.ui.signin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.extensions.isPasswordValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
) : ViewModel() {
    private val _signInUiState = MutableStateFlow(UiState(SignInScreenData()))
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
