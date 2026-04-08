package com.alphagamingarcade.feature.user.ui.editprofile

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

/**
 * [ViewModel] for [EditProfileScreen].
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    // private val userRepository: UserRepository,
) : ViewModel() {

    private val _editProfileUiState = MutableStateFlow(UiState(EditProfileScreenData()))
    val editProfileUiState = _editProfileUiState.asStateFlow()

    fun updateFullName(fullName: String) {
        _editProfileUiState.updateState {
            copy(
                fullName = TextFieldData(
                    value = fullName,
                    errorMessage = when {
                        fullName.isBlank() -> "Full name is required"
                        fullName.length < 3 -> "Full name must be at least 3 characters"
                        else -> null
                    },
                ),
            )
        }
    }

    fun updateUsername(username: String) {
        _editProfileUiState.updateState {
            copy(
                username = TextFieldData(
                    value = username,
                    errorMessage = when {
                        username.isBlank() -> "Username is required"
                        username.length < 3 -> "Username must be at least 3 characters"
                        username.contains(" ") -> "Username must not contain spaces"
                        else -> null
                    },
                ),
            )
        }
    }

    fun updateEmail(email: String) {
        _editProfileUiState.updateState {
            copy(
                email = TextFieldData(
                    value = email,
                    errorMessage = if (email.isEmailValid()) null else "Email Not Valid",
                ),
            )
        }
    }

    fun saveProfile() {
        val screenData = _editProfileUiState.value.data

        val fullNameError = when {
            screenData.fullName.value.isBlank() -> "Full name is required"
            screenData.fullName.value.length < 3 -> "Full name must be at least 3 characters"
            else -> null
        }

        val usernameError = when {
            screenData.username.value.isBlank() -> "Username is required"
            screenData.username.value.length < 3 -> "Username must be at least 3 characters"
            screenData.username.value.contains(" ") -> "Username must not contain spaces"
            else -> null
        }

        val emailError =
            if (screenData.email.value.isEmailValid()) null else "Email Not Valid"

        _editProfileUiState.updateState {
            copy(
                fullName = fullName.copy(errorMessage = fullNameError),
                username = username.copy(errorMessage = usernameError),
                email = email.copy(errorMessage = emailError),
            )
        }

        val hasError = fullNameError != null || usernameError != null || emailError != null
        if (hasError) return

        // TODO:
        // call repository here if needed
        // example:
        // viewModelScope.launch {
        //     _editProfileUiState.update { it.copy(loading = true) }
        //     runCatching {
        //         userRepository.updateProfile(
        //             fullName = screenData.fullName.value,
        //             username = screenData.username.value,
        //             email = screenData.email.value
        //         )
        //     }.onSuccess {
        //         _editProfileUiState.update { it.copy(loading = false) }
        //     }.onFailure { throwable ->
        //         _editProfileUiState.update {
        //             it.copy(
        //                 loading = false,
        //                 error = OneTimeEvent(throwable)
        //             )
        //         }
        //     }
        // }
    }
}

/**
 * Data for [EditProfileScreen].
 *
 * @param fullName [TextFieldData].
 * @param username [TextFieldData].
 * @param email [TextFieldData].
 */
@Immutable
data class EditProfileScreenData(
    val fullName: TextFieldData = TextFieldData(String()),
    val username: TextFieldData = TextFieldData(String()),
    val email: TextFieldData = TextFieldData(String()),
)