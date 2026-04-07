package com.alphagamingarcade.feature.editprofile.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.data.repository.home.HomeRepository
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _editProfileUiState = MutableStateFlow(UiState({}))
    val editProfileUiState = _editProfileUiState
        .onStart { }
        .stateInDelayed(UiState({}), viewModelScope)
}
