package com.alphagamingarcade.feature.auth.ui.setupprofile

import com.alphagamingarcade.feature.auth.R
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.data.repository.MembersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.extensions.isAccountFormatValid
import com.alphagamingarcade.core.extensions.isAccountLengthValid
import com.alphagamingarcade.core.extensions.isNicknameFormatValid
import com.alphagamingarcade.core.extensions.isNicknameLengthValid
import com.alphagamingarcade.core.extensions.isNicknameValid
import com.alphagamingarcade.core.ui.utils.UiText
import com.alphagamingarcade.core.ui.utils.toUiText
import com.alphagamingarcade.core.ui.utils.updateWithAppResult

@HiltViewModel
class SetupProfileViewModel @Inject constructor(
    private  val membersRepository: MembersRepository
) : ViewModel() {

    private val _setUpProfileUiState = MutableStateFlow(UiState(SetupProfileScreenData()))
    val setUpProfileUiState = _setUpProfileUiState.asStateFlow()

    private val _events = kotlinx.coroutines.channels.Channel<SetupProfileEvent>()
    val events = _events.receiveAsFlow()

    fun updateAccount(account: String) {
        _setUpProfileUiState.updateState {
            copy(
                account = TextFieldData(
                    value = account,
                    errorMessage = when {
                        !account.isAccountLengthValid() ->
                            UiText.StringResource(R.string.account_length_error)

                        !account.isAccountFormatValid() ->
                            UiText.StringResource(R.string.account_format_error)

                        else -> null
                    }
                )
            )
        }
    }

    fun updateNickname(nickname: String) {
        _setUpProfileUiState.updateState {
            copy(
                nickname = TextFieldData(
                    value = nickname,
                    errorMessage = when {
                        !nickname.isNicknameValid() ->
                            UiText.StringResource(R.string.nickname_length_error)
                        else -> null
                    }
                )
            )
        }
    }

    fun updateDob(dob: String) {
        _setUpProfileUiState.updateState {
            copy(
                dob = TextFieldData(
                    value = dob,
                    errorMessage = try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.isLenient = false
                        val birthDate = sdf.parse(dob) ?: throw Exception()

                        val birthCalendar = Calendar.getInstance().apply { time = birthDate }
                        val today = Calendar.getInstance()

                        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                            age--
                        }

                        if (age < 18)
                            UiText.StringResource(R.string.age_requirement_error)
                        else null

                    } catch (e: Exception) {
                        UiText.StringResource(R.string.invalid_date)
                    }
                )
            )
        }
    }

    fun onSetupProfile() {
        if (!validate()) return;

        _setUpProfileUiState.updateWithAppResult {
            val result = membersRepository.createMember(
                account = account.value,
                nickname = nickname.value,
                dateOfBirth = dob.value
            )

            when (result) {
                is AppResult.Success -> {
                    _events.send(SetupProfileEvent.OnProfileSetupComplete)
                }

                is AppResult.Error -> {
                    val accountError = result.errors
                        ?.firstOrNull { it.field.equals("account", ignoreCase = true) }
                        ?.toUiText()

                    val nicknameError = result.errors
                        ?.firstOrNull { it.field.equals("nickname", ignoreCase = true) }
                        ?.toUiText()

                    val dobError = result.errors
                        ?.firstOrNull {
                            it.field.equals("dob", ignoreCase = true) ||
                                    it.field.equals("dateOfBirth", ignoreCase = true) ||
                                    it.field.equals("date_of_birth", ignoreCase = true)
                        }
                        ?.toUiText()

                    _setUpProfileUiState.updateState {
                        copy(
                            account = TextFieldData(
                                value = account.value,
                                errorMessage = accountError
                            ),
                            nickname = TextFieldData(
                                value = nickname.value,
                                errorMessage = nicknameError
                            ),
                            dob = TextFieldData(
                                value = dob.value,
                                errorMessage = dobError
                            )
                        )
                    }
                }
            }

            result
        }
    }

    private fun validate(): Boolean {
        val data = _setUpProfileUiState.value.data

        val accountError = when {
            !data.account.value.isAccountLengthValid() ->
                UiText.StringResource(R.string.account_length_error)

            !data.account.value.isAccountFormatValid() ->
                UiText.StringResource(R.string.account_format_error)

            else -> null
        }


        val nicknameError = when {
            data.nickname.value.isBlank() ->
                UiText.StringResource(R.string.nickname_required)

            !data.nickname.value.isNicknameLengthValid() ->
                UiText.StringResource(R.string.nickname_length_error)

            !data.nickname.value.isNicknameFormatValid() ->
                UiText.StringResource(R.string.nickname_invalid)

            else -> null
        }

        val dobError = validateDob(data.dob.value)

        val isValid = listOf(accountError, nicknameError, dobError).all { it == null }

        if (!isValid) {
            _setUpProfileUiState.updateState {
                copy(
                    account = account.copy(errorMessage = accountError),
                    nickname = nickname.copy(errorMessage = nicknameError),
                    dob = dob.copy(errorMessage = dobError),
                )
            }
        }

        return isValid
    }

    private fun validateDob(dob: String): UiText? {
        if (dob.isBlank()) return UiText.StringResource(R.string.dob_required)

        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false

            val birthDate = sdf.parse(dob) ?: return UiText.StringResource(R.string.invalid_date)

            val birthCalendar = Calendar.getInstance().apply { time = birthDate }
            val today = Calendar.getInstance()

            if (birthCalendar.after(today)) {
                return UiText.StringResource(R.string.dob_future_error)
            }

            var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            if (age < 18)
                UiText.StringResource(R.string.age_requirement_error)
            else null

        } catch (e: Exception) {
            UiText.StringResource(R.string.invalid_date)
        }
    }
}

@Immutable
data class SetupProfileScreenData(
    val account: TextFieldData = TextFieldData(String()),       // NEW - 4-16 chars, alphanumeric
    val nickname: TextFieldData = TextFieldData(String()),      // RENAMED from name
    val dob: TextFieldData = TextFieldData(String()),           // NEW - date, must be 18+
)

sealed class SetupProfileEvent {
    object OnProfileSetupComplete : SetupProfileEvent()
}
