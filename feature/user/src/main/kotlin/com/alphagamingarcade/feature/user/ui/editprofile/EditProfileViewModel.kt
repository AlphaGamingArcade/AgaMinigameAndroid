package com.alphagamingarcade.feature.user.ui.editprofile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.MemberRepository
import com.alphagamingarcade.core.data.repository.MemberRepositoryImpl
import com.alphagamingarcade.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.extensions.isEmailValid
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWith
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for [EditProfileScreen].
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private  val memberRepository: MemberRepository,
    private  val profileRepository: ProfileRepository
) : ViewModel() {

    private val _editProfileUiState = MutableStateFlow(UiState(EditProfileScreenData()))
    val editProfileUiState = _editProfileUiState.asStateFlow()

    private val _successEvent = Channel<Unit>(Channel.BUFFERED)
    val successEvent = _successEvent.receiveAsFlow()

    fun updateNickname(nickname: String) {
        _editProfileUiState.updateState {
            copy(
                nickname = TextFieldData(
                    value = nickname,
                    errorMessage = when {
                        nickname.isBlank() -> "Username is required"
                        nickname.length < 3 -> "Username must be at least 3 characters"
                        nickname.contains(" ") -> "Username must not contain spaces"
                        else -> null
                    },
                ),
            )
        }
    }

    fun saveProfile() {
        val screenData = _editProfileUiState.value.data

        val nicknameError = when {
            screenData.nickname.value.isBlank() -> "Nickname is required"
            screenData.nickname.value.length < 3 -> "Nickname must be at least 3 characters"
            else -> null
        }

        _editProfileUiState.updateState {
            copy(
                nickname = nickname.copy(errorMessage = nicknameError),
            )
        }

        val hasError = nicknameError != null
        if (hasError) return

        _editProfileUiState.updateWith {
            memberRepository.updateMember(
                nickname = nickname.value
            ).onSuccess {
                _successEvent.send(Unit)
            }
        }
    }
}

/**
 * Data for [EditProfileScreen].
 *
 * @param nickname [TextFieldData].
 */
@Immutable
data class EditProfileScreenData(
    val nickname: TextFieldData = TextFieldData(String()),
)