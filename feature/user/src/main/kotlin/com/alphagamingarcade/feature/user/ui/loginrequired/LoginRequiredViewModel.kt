package com.alphagamingarcade.feature.user.ui.loginrequired

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginRequiredScreenData(
    val isLoading: Boolean = false,
)

@HiltViewModel
class LoginRequiredViewModel @Inject constructor() : ViewModel() {

    private val _loginRequiredUiState = MutableStateFlow(UiState(LoginRequiredScreenData()))
    val loginRequiredUiState = _loginRequiredUiState
        .onStart { }
        .stateInDelayed(UiState(LoginRequiredScreenData()), viewModelScope)

    fun onLoginClick() {
        // Trigger login navigation via event or callback
    }

    fun onSignUpClick() {
        // Trigger sign up navigation via event or callback
    }
}