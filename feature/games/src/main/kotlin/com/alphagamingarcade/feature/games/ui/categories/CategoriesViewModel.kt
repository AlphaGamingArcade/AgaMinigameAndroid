package com.alphagamingarcade.feature.games.ui.categories

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private  val gamesRepository: GamesRepository
) : ViewModel() {

    private var categoryName: String? = null

    private val _uiState = MutableStateFlow(UiState(CategoriesScreenData()))
    val uiState = _uiState.stateInDelayed(UiState(CategoriesScreenData()), viewModelScope)

    fun initialize(category: String) {
        if (categoryName != null) return // prevent re-fetch on recomposition
        categoryName = category
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            gamesRepository.getGames(
                pageNumber = 1,
                pageSize = 50,
                category = categoryName
            )
                .catch { e ->
                    _uiState.value = UiState(
                        data = CategoriesScreenData(
                            games = emptyList(),
                            isLoading = false,
                        ),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    _uiState.value = UiState(
                        CategoriesScreenData(
                            games = games,
                            isLoading = false,
                        ),
                    )
                }
        }
    }
}

@Immutable
data class CategoriesScreenData(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = true, // ← add this
)