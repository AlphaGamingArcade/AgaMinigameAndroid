package com.alphagamingarcade.feature.user.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.asOneTimeEvent
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateWith
import com.alphagamingarcade.data.model.profile.Profile
import com.alphagamingarcade.data.repository.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * [ViewModel] for [UserScreen].
 *
 * @param profileRepository [ProfileRepository].
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _profileUiState = MutableStateFlow(UiState(Profile()))
    val profileUiState = _profileUiState
        .onStart { updateProfileData() }
        .stateInDelayed(UiState(Profile()), viewModelScope)

    private fun updateProfileData() {
        profileRepository.getProfile()
            .map { profileScreenData -> UiState(profileScreenData) }
            .onEach { data -> _profileUiState.update { data } }
            .catch { e -> UiState(Profile(), error = e.asOneTimeEvent()) }
            .launchIn(viewModelScope)
    }

    fun signOut() {
        _profileUiState.updateWith { profileRepository.signOut() }
    }
}