package com.alphagamingarcade.feature.user.ui.editprofile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.data.repository.MembersRepository
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.extensions.isNicknameFormatValid
import com.alphagamingarcade.core.extensions.isNicknameLengthValid
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.UiText
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWithAppResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import  com.alphagamingarcade.feature.user.R

/**
 * [ViewModel] for [EditProfileScreen].
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private  val membersRepository: MembersRepository,
    private  val profileRepository: ProfileRepository
) : ViewModel() {

    private val _editProfileUiState = MutableStateFlow(UiState(EditProfileScreenData()))
    val editProfileUiState = _editProfileUiState.asStateFlow()

    private val _successEvent = Channel<Unit>(Channel.BUFFERED)
    val successEvent = _successEvent.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile()
                .collect { profile ->
                    _editProfileUiState.updateState {
                        copy(
                            nickname = TextFieldData(value = profile.nickname),
                        )
                    }
                }
        }
    }

    fun updateNickname(nickname: String) {
        _editProfileUiState.updateState {
            copy(
                nickname = this.nickname.copy(
                    value = nickname,
                    errorMessage = when {
                        nickname.isBlank() -> null
                        !nickname.isNicknameLengthValid() ->
                            UiText.StringResource(R.string.transactions)
                        !nickname.isNicknameFormatValid() ->
                            UiText.StringResource(R.string.nickname_format_invalid)
                        else -> null
                    },
                ),
            )
        }
    }

    fun saveProfile() {
        if (!validate()) return

        _editProfileUiState.updateWithAppResult {
            val result = membersRepository.updateMember(
                nickname = nickname.value
            )

            when (result) {
                is AppResult.Success -> {
                    _successEvent.send(Unit)
                }

                is AppResult.Error -> {
                    val nicknameApiError = result.errors
                        ?.firstOrNull { it.field.equals("nickname", ignoreCase = true) }
                        ?.message
                        ?.let { UiText.DynamicString(it) }

                    _editProfileUiState.updateState {
                        copy(
                            nickname = nickname.copy(
                                errorMessage = nicknameApiError ?: nickname.errorMessage
                            )
                        )
                    }
                }
            }

            result
        }
    }

    private fun validate(): Boolean {
        val data = _editProfileUiState.value.data

        val nicknameError = when {
            data.nickname.value.isBlank() ->
                UiText.StringResource(R.string.nickname_required)

            !data.nickname.value.isNicknameLengthValid() ->
                UiText.StringResource(R.string.nickname_length_invalid)

            !data.nickname.value.isNicknameFormatValid() ->
                UiText.StringResource(R.string.nickname_format_invalid)

            else -> null
        }

        val isValid = nicknameError == null

        if (!isValid) {
            _editProfileUiState.updateState {
                copy(
                    nickname = nickname.copy(errorMessage = nicknameError)
                )
            }
        }

        return isValid
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