package com.alphagamingarcade.feature.gamedetail.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for [GameDetailScreen].
 */
@HiltViewModel
class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gamesRepository: GamesRepository
) : ViewModel() {
    private val gameId: String = savedStateHandle["gameId"] ?: ""
    private val _gameDetailUiState = MutableStateFlow(UiState(GameDetailScreenData()))
    val gameDetailUiState = _gameDetailUiState.asStateFlow()

    init {
        getGameDetail()
    }

    fun getGameDetail() {
        viewModelScope.launch {
            try {
                _gameDetailUiState.value = UiState(
                    data = _gameDetailUiState.value.data,
                    loading = true,
                    error = OneTimeEvent(null),
                )

                gamesRepository.getGame(gameId.toInt())
                    .onSuccess { game ->
                        _gameDetailUiState.value = UiState(
                            data = GameDetailScreenData(game = GameDetailUiModel(
                                id = game.id,
                                title = game.name,
                                category = game.category,
                                playerCountLabel = game.playerCount.toString(),
                                bannerUrl = game.imageUrl,
                                iconUrl = game.imageUrl,
                                screenshotUrls = emptyList(),
                                description = game.name,
                                isFavorite = false,
                            )),
                            loading = false,
                        )
                    }.onFailure { throwable ->
                        _gameDetailUiState.value = UiState(
                            data = _gameDetailUiState.value.data,
                            loading = false,
                            error = OneTimeEvent(throwable),
                        )
                    }
            } catch (e: Exception) {
                _gameDetailUiState.value = UiState(
                    data = _gameDetailUiState.value.data,
                    loading = false,
                    error = OneTimeEvent(e),
                )
            }
        }
    }

    fun toggleFavorite() {
        val current = _gameDetailUiState.value.data

        _gameDetailUiState.value = _gameDetailUiState.value.copy(
            data = current.copy(
                game = current.game.copy(
                    isFavorite = !current.game.isFavorite,
                ),
            ),
        )
    }

    fun selectScreenshot(imageUrl: String) {
        _gameDetailUiState.value = _gameDetailUiState.value.copy(
            data = _gameDetailUiState.value.data.copy(
                selectedScreenshotUrl = imageUrl,
            ),
        )
    }

    fun openSimilarGame(gameId: String) {
        // optional hook if you want analytics or preload later
    }
}

/**
 * Data for [GameDetailScreen].
 */
@Immutable
data class GameDetailScreenData(
    val game: GameDetailUiModel = GameDetailUiModel(),
    val selectedScreenshotUrl: String = "",
    val similarGames: List<SimilarGameUiModel> = emptyList(),
)

@Immutable
data class GameDetailUiModel(
    val id: Int = 0,
    val title: String = "",
    val category: String = "",
    val playerCountLabel: String = "",
    val bannerUrl: String = "",
    val iconUrl: String = "",
    val screenshotUrls: List<String> = emptyList(),
    val description: String = "",
    val isFavorite: Boolean = false,
)

@Immutable
data class SimilarGameUiModel(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val thumbnailUrl: String = "",
)

