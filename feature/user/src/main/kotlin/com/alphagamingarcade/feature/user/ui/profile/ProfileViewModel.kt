package com.alphagamingarcade.feature.user.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.model.Profile
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(UiState(Profile()))
    val profileUiState = _profileUiState
        .onStart { updateProfileData() }
        .stateInDelayed(UiState(Profile()), viewModelScope)

    private fun updateProfileData() {
//        profileRepository.getProfile()
//            .map { profileScreenData -> UiState(profileScreenData) }
//            .onEach { data -> _profileUiState.update { data } }
//            .catch { e -> UiState(Profile(), error = e.asOneTimeEvent()) }
//            .launchIn(viewModelScope)
    }
}