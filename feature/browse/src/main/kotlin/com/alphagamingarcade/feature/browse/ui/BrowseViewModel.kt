package com.alphagamingarcade.feature.browse.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.getPreferredLocale
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _language = MutableStateFlow(getPreferredLanguage())
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _browseUiState = MutableStateFlow(UiState(BrowseScreenData()))
    val browseUiState = _browseUiState
        .onStart {
            loadGames()
        }
        .stateInDelayed(UiState(BrowseScreenData()), viewModelScope)

    fun refresh(){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                loadGames()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadGames(){
        viewModelScope.launch {
            gamesRepository.getGames(
                pageNumber = 1,
                pageSize = 50,
            )
                .catch { e ->
                    _browseUiState.value = UiState(
                        data = BrowseScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    val currentData = _browseUiState.value.data
                    _browseUiState.value = UiState(
                        data = currentData.copy(allGames = games)
                    )
                }
        }
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

/**
 * Browse screen data.
 *
 * @param allGames     Full list used for search and filtering.
 * @param featuredGames Games shown in the hero featured banner.
 * @param hotGames      Currently hot/popular games.
 * @param newGames      Recently released games.
 */
@Immutable
data class BrowseScreenData(
    val allGames: List<Game> = emptyList(),
) {
    val featuredGames get() = allGames.take(5)
    val hotGames get() = allGames.filter { it.isTrending }
    val newGames get() = allGames.filter { it.isLatest }
}