package com.alphagamingarcade.feature.auth.ui.setupprofile

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
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+\$")
        _setUpProfileUiState.updateState {
            copy(
                account = TextFieldData(
                    value = account,
                    errorMessage = when {
                        account.length !in 4..16 ->
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
        _setUpProfileUiState.updateState {
            copy(
                nickname = TextFieldData(
                    value = nickname,
                    errorMessage = when {
                        nickname.length !in 4..64 ->
                            "Nickname must be between 4 and 64 characters"
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

    fun onSetupProfile() {
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
                        ?.message

                    val nicknameError = result.errors
                        ?.firstOrNull { it.field.equals("nickname", ignoreCase = true) }
                        ?.message

                    val dobError = result.errors
                        ?.firstOrNull {
                            it.field.equals("dob", ignoreCase = true) ||
                                    it.field.equals("dateOfBirth", ignoreCase = true) ||
                                    it.field.equals("date_of_birth", ignoreCase = true)
                        }
                        ?.message

                    _setUpProfileUiState.updateState {
                        copy(
                            account = TextFieldData(
                                value = account.value,
                                errorMessage = accountError ?: account.errorMessage
                            ),
                            nickname = TextFieldData(
                                value = nickname.value,
                                errorMessage = nicknameError ?: nickname.errorMessage
                            ),
                            dob = TextFieldData(
                                value = dob.value,
                                errorMessage = dobError ?: dob.errorMessage
                            )
                        )
                    }
                }
            }

            result
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
