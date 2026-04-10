package com.alphagamingarcade.feature.auth.ui.signup

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _signUpUiState = MutableStateFlow(UiState(SignUpScreenData()))
    val signUpUiState = _signUpUiState.asStateFlow()

    fun updateAccount(account: String) {
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+\$")
        _signUpUiState.updateState {
            copy(
                account = TextFieldData(
                    value = account,
                    errorMessage = when {
                        account.length < 4 || account.length > 16 ->
                            "Account must be between 4 and 16 characters"
                        !regex.matches(account) ->
                            "Account must contain both letters and numbers"
                        else -> null
                    }
                )
            )
        }
    }

    fun updateNickname(nickname: String) {
        _signUpUiState.updateState {
            copy(
                nickname = TextFieldData(
                    value = nickname,
                    errorMessage = when {
                        nickname.length < 4 || nickname.length > 64 ->
                            "Nickname must be between 4 and 64 characters"
                        else -> null
                    }
                )
            )
        }
    }

    fun updateDob(dob: String) {
        _signUpUiState.updateState {
            copy(
                dob = TextFieldData(
                    value = dob,
                    errorMessage = try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.isLenient = false
                        val birthDate = sdf.parse(dob) ?: throw Exception("Invalid date")

                        val birthCalendar = Calendar.getInstance().apply { time = birthDate }
                        val today = Calendar.getInstance()

                        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                            age--
                        }

                        if (age < 18) "You must be 18 years or older" else null
                    } catch (e: Exception) {
                        "Invalid date"
                    }
                )
            )
        }
    }

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
}

/**
 * Data for [SignUpScreen].
 *
 * @param name [TextFieldData].
 * @param email [TextFieldData].
 * @param password [TextFieldData].
 */
@Immutable
data class SignUpScreenData(
    val account: TextFieldData = TextFieldData(String()),       // NEW - 4-16 chars, alphanumeric
    val nickname: TextFieldData = TextFieldData(String()),      // RENAMED from name
    val dob: TextFieldData = TextFieldData(String()),           // NEW - date, must be 18+
    val email: TextFieldData = TextFieldData(String()),
    val password: TextFieldData = TextFieldData(String()),
    val confirmPassword: TextFieldData = TextFieldData(String()) // NEW - must match password
)