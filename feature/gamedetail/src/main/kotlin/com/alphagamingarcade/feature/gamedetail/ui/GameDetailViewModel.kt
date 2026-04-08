package com.alphagamingarcade.feature.gamedetail.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    // private val gamesRepository: GamesRepository,
) : ViewModel() {

    private val gameId: String = savedStateHandle["gameId"] ?: ""

    private val _gameDetailUiState = MutableStateFlow(UiState(GameDetailScreenData()))
    val gameDetailUiState = _gameDetailUiState.asStateFlow()

    init {
        getGameDetail()
    }

    fun getGameDetail() {
        viewModelScope.launch {
            _gameDetailUiState.value = _gameDetailUiState.value.copy(
                loading = true,
                error = OneTimeEvent(null),
            )

            runCatching {
                // TODO replace with repository call
                mockGameDetail(gameId)
            }.onSuccess { data ->
                _gameDetailUiState.value = UiState(
                    data = data,
                    loading = false,
                )
            }.onFailure { throwable ->
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    loading = false,
                    error = OneTimeEvent(throwable),
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

    private fun mockGameDetail(gameId: String): GameDetailScreenData {
        val screenshots = listOf(
            "https://picsum.photos/900/500?random=101",
            "https://picsum.photos/900/500?random=102",
            "https://picsum.photos/900/500?random=103",
            "https://picsum.photos/900/500?random=104",
        )

        return GameDetailScreenData(
            game = GameDetailUiModel(
                id = if (gameId.isBlank()) "wild-west" else gameId,
                title = "Wild West",
                category = "Paylines",
                playerCountLabel = "5M+ players",
                bannerUrl = "https://picsum.photos/1200/600?random=100",
                iconUrl = "https://picsum.photos/200/200?random=99",
                screenshotUrls = screenshots,
                description = "Step into a dusty frontier adventure packed with classic slot thrills, western-themed symbols, and fast-paced excitement. Explore a beautifully styled old west setting while chasing rewarding combinations and immersive game moments designed for both casual and returning players.",
                isFavorite = true,
            ),
            selectedScreenshotUrl = screenshots.first(),
            similarGames = listOf(
                SimilarGameUiModel(
                    id = "desert-gold",
                    title = "Desert Gold",
                    category = "Adventure",
                    thumbnailUrl = "https://picsum.photos/300/300?random=110",
                ),
                SimilarGameUiModel(
                    id = "cowboy-fortune",
                    title = "Cowboy Fortune",
                    category = "Paylines",
                    thumbnailUrl = "https://picsum.photos/300/300?random=111",
                ),
                SimilarGameUiModel(
                    id = "wild-sheriff",
                    title = "Wild Sheriff",
                    category = "Classic",
                    thumbnailUrl = "https://picsum.photos/300/300?random=112",
                ),
                SimilarGameUiModel(
                    id = "gold-rush-west",
                    title = "Gold Rush West",
                    category = "Jackpot",
                    thumbnailUrl = "https://picsum.photos/300/300?random=113",
                ),
            ),
        )
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
    val id: String = "",
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