package com.alphagamingarcade.feature.auth.ui.checkyouremail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



/**
 * [ViewModel] for [CheckYourEmailScreen].
 *
 * @param authRepository [AuthRepository].
 */
@HiltViewModel
class CheckYourEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _checkYourEmailUiState = MutableStateFlow(UiState(CheckYourEmailData()))
    val checkYourEmailUiState = _checkYourEmailUiState.asStateFlow()

    fun resendEmail(email: String) {
        viewModelScope.launch {
//            _checkYourEmailUiState.setLoading()
        }
    }
}

/**
 * Data for [CheckYourEmailScreen].
 */
@Immutable
data class CheckYourEmailData(
    val placeholder: Unit = Unit,
)