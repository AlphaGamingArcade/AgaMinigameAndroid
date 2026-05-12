package com.alphagamingarcade.feature.games.ui.categories

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _language = MutableStateFlow(getPreferredLanguage())
    val language: StateFlow<Language> = _language.asStateFlow()

    private var categoryName: String? = null

    private val initialState = UiState(
        data = CategoriesScreenData(),
        loading = true
    )

    private val _uiState = MutableStateFlow(initialState)

    val uiState = _uiState.stateInDelayed(
        initialValue = initialState,
        scope = viewModelScope
    )

    fun initialize(category: String) {
        if (categoryName == category) return

        categoryName = category
        fetchGames(showFullScreenLoading = true)
    }

    fun refresh() {
        fetchGames(showFullScreenLoading = false)
    }

    private fun fetchGames(showFullScreenLoading: Boolean) {
        viewModelScope.launch {
            if (showFullScreenLoading) {
                _uiState.value = _uiState.value.copy(
                    loading = true,
                    error = OneTimeEvent(null)
                )
            } else {
                _isRefreshing.value = true
            }

            try {
                gamesRepository.getGames(
                    pageNumber = 1,
                    pageSize = 50,
                    category = categoryName
                )
                    .catch { e ->
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            error = OneTimeEvent(e)
                        )
                    }
                    .collect { games ->
                        _uiState.value = UiState(
                            data = CategoriesScreenData(games = games),
                            loading = false
                        )
                    }
            } finally {
                _isRefreshing.value = false
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

@Immutable
data class CategoriesScreenData(
    val games: List<Game> = emptyList()
)