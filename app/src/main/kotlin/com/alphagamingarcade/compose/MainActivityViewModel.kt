package com.alphagamingarcade.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import com.alphagamingarcade.core.extensions.asOneTimeEvent
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userPreferencesDataSource: UserPreferencesDataSource,
) : ViewModel() {
    /**
     * Represents the state of the UI for user data.
     */
    val uiState: StateFlow<UiState<UserDataPreferences>> =
        userPreferencesDataSource.getUserDataPreferences()
            .map { userData -> UiState(userData) }
            .catch { e -> UiState(UserDataPreferences(), error = e.asOneTimeEvent()) }
            .stateInDelayed(UiState(UserDataPreferences(), loading = true), viewModelScope)
}