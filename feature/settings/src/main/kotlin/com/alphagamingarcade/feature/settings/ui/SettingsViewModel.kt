package com.alphagamingarcade.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.model.DarkThemeConfig
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.data.model.Settings
import com.alphagamingarcade.core.data.repository.SettingsRepository
import com.alphagamingarcade.core.extensions.asOneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.getPreferredLocale
import com.alphagamingarcade.core.ui.utils.setLanguagePreference
import com.alphagamingarcade.core.ui.utils.updateWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private  val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settingsUiState = MutableStateFlow(UiState(Settings()))
    val settingsUiState = _settingsUiState
        .onStart { updateSettings() }
        // Can't use stateInDelayed here because language change doesn't update
        // after activity restart, as it caches value for 5 seconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState(Settings()))

    private fun updateSettings() {
        settingsRepository.getSettings()
            .map { UiState(it.copy(language = getPreferredLanguage())) }
            .onEach { state -> _settingsUiState.update { state } }
            .catch { e -> UiState(Settings(), error = e.asOneTimeEvent()) }
            .launchIn(viewModelScope)
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        _settingsUiState.updateWith {
            settingsRepository.setDarkThemeConfig(
                darkThemeConfig,
            )
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        _settingsUiState.updateWith {
            settingsRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun updateLanguagePreference(language: Language) {
        setLanguagePreference(language.code)
    }

    fun signOut() {
        _settingsUiState.updateWith { settingsRepository.signOut() }
    }

    private fun getPreferredLanguage(): Language {
        val preferredLanguage = getPreferredLocale().language
        return when (preferredLanguage) {
            "ko" -> Language.KOREAN
            "zh" -> Language.CHINESE
            "ja" -> Language.JAPANESE
            else -> Language.ENGLISH
        }
    }
}
