package com.alphagamingarcade.feature.auth.ui.checkyouremail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateWith
import com.alphagamingarcade.feature.auth.ui.signin.SignInEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckYourEmailViewModel @Inject constructor(
    private  val authRepository: AuthRepository
) : ViewModel() {
    private var hasStartedInitialCooldown = false
    private var pollingJob: Job? = null
    private val _checkYourEmailUiState = MutableStateFlow(UiState(CheckYourEmailData(
        canResendEmail = false,
        resendCooldownSeconds = 120
    )))
    val checkYourEmailUiState = _checkYourEmailUiState.asStateFlow()

    fun onScreenEntered() {
        if (hasStartedInitialCooldown) return
        hasStartedInitialCooldown = true
        startResendCooldown()
    }

    /**
     * Start polling to check if email has been verified
     * @param email Email address to check verification status for
     * @param pollingIntervalMs Interval between polls in milliseconds (default: 5 seconds)
     * @param maxAttemptsMs Maximum time to poll in milliseconds (default: 15 minutes)
     */
    fun startVerificationPolling(
        email: String,
        pollingIntervalMs: Long = 5000L,
        maxAttemptsMs: Long = 15 * 60 * 1000L, // 15 minutes
    ) {
        if (pollingJob?.isActive == true) return  // or cancel and restart
        pollingJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()

            while (isActive) {
                val elapsedTime = System.currentTimeMillis() - startTime
                if (elapsedTime > maxAttemptsMs) return@launch

                val result = authRepository.getEmailStatus(email)
                result.onSuccess { isVerified ->
                    _checkYourEmailUiState.update { current ->
                        current.copy(
                            data = current.data.copy(isEmailVerified = isVerified),
                        )
                    }
                    if (isVerified) {
                        return@launch
                    }
                }

                delay(pollingIntervalMs)
            }
        }
    }

    private fun startResendCooldown(durationSeconds: Int = 120) {
        viewModelScope.launch {
            for (seconds in durationSeconds downTo 0) {
                _checkYourEmailUiState.update { current ->
                    current.copy(
                        data = current.data.copy(
                            canResendEmail = seconds == 0,
                            resendCooldownSeconds = seconds
                        )
                    )
                }

                if (seconds > 0) {
                    delay(1000)
                }
            }
        }
    }

    fun resendEmail(email: String) {
        val current = _checkYourEmailUiState.value.data
        if (!current.canResendEmail) return

        _checkYourEmailUiState.updateWith {
            val result = authRepository.resendVerifyEmail(email)

            result.onSuccess {
                startResendCooldown()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Polling will automatically stop when ViewModel is cleared
    }
}

/**
 * Data for [CheckYourEmailScreen].
 */
@Immutable
data class CheckYourEmailData(
    val isLoading: Boolean = false,
    val placeholder: Unit = Unit,
    val isEmailVerified: Boolean = false,
    // Resend state
    val canResendEmail: Boolean = true,
    val resendCooldownSeconds: Int = 0
)
