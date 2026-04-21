package com.alphagamingarcade.feature.auth.ui.setupprofile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.alphagamingarcade.core.data.repository.MemberRepository
import com.alphagamingarcade.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.TextFieldData
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateState
import com.alphagamingarcade.core.ui.utils.updateWith
import com.alphagamingarcade.feature.auth.ui.signin.SignInEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@HiltViewModel
class SetupProfileViewModel @Inject constructor(
    private  val memberRepository: MemberRepository
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
        _setUpProfileUiState.updateWith {
            val result =memberRepository.createMember(
                account = account.value,
                nickname = nickname.value,
                dateOfBirth = dob.value
            )

            if (result.isSuccess){
                _events.send(SetupProfileEvent.OnProfileSetupComplete)
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
