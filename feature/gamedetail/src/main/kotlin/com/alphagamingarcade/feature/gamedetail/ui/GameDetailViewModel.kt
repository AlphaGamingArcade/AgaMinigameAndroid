package com.alphagamingarcade.feature.gamedetail.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.data.repository.MembersRepository
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.getPreferredLocale
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gamesRepository: GamesRepository,
    private val membersRepository: MembersRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val gameId: String = savedStateHandle["gameId"] ?: ""

    private val _gameDetailUiState = MutableStateFlow(UiState(GameDetailScreenData()))
    val gameDetailUiState = _gameDetailUiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<OneTimeEvent<PlayScreenArgs>?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    private val _language = MutableStateFlow(getPreferredLanguage())
    val language: StateFlow<Language> = _language.asStateFlow()


    init {
        getGameDetail()
        getSimilarGames()
    }

    fun getGameDetail() {
        viewModelScope.launch {
            try {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    loading = true,
                    error = OneTimeEvent(null),
                )

                gamesRepository.getGame(gameId.toInt())
                    .onSuccess { game ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            data = _gameDetailUiState.value.data.copy(
                                game = GameDetailUiModel(
                                    id = game.id,
                                    title = game.name.get(language.value.code),
                                    category = game.category,
                                    playerCountLabel = game.playerCount.toString(),
                                    bannerUrl = game.imageUrl,
                                    iconUrl = game.imageUrl,
                                    screenshotUrls = emptyList(),
                                    description = game.description.get(language.value.code),
                                    isFavorite = game.isFavorite ?: false,
                                ),
                            ),
                            loading = false,
                            error = OneTimeEvent(null),
                        )
                    }
                    .onFailure { throwable ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            loading = false,
                            error = OneTimeEvent(throwable),
                        )
                    }
            } catch (e: Exception) {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    loading = false,
                    error = OneTimeEvent(e),
                )
            }
        }
    }

    fun getSimilarGames() {
        viewModelScope.launch {
            try {
                gamesRepository.getSimilarGames(
                    gameId = gameId.toInt(),
                    pageNumber = 1,
                    pageSize = 10,
                )
                    .onSuccess { games ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            data = _gameDetailUiState.value.data.copy(
                                similarGames = games.map { game ->
                                    SimilarGameUiModel(
                                        id = game.id,
                                        title = game.name.get(language.value.code),
                                        category = game.category,
                                        thumbnailUrl = game.imageUrl,
                                    )
                                },
                            ),
                            loading = false,
                            error = OneTimeEvent(null),
                        )
                    }
                    .onFailure { throwable ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            loading = false,
                            error = OneTimeEvent(throwable),
                        )
                    }
            } catch (e: Exception) {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    loading = false,
                    error = OneTimeEvent(e),
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentState = _gameDetailUiState.value
            val currentGame = currentState.data.game
            val newFavoriteState = !currentGame.isFavorite

            _gameDetailUiState.value = currentState.copy(
                data = currentState.data.copy(
                    game = currentGame.copy(
                        isFavorite = newFavoriteState,
                    ),
                ),
            )

            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                val result = if (newFavoriteState) {
                    membersRepository.addFavorite(resolvedMemberId, gameId.toInt())
                } else {
                    membersRepository.removeFavorite(resolvedMemberId, gameId.toInt())
                }

                result.onFailure { e ->
                    _gameDetailUiState.value = _gameDetailUiState.value.copy(
                        data = _gameDetailUiState.value.data.copy(
                            game = currentGame,
                        ),
                        error = OneTimeEvent(e),
                    )
                }
            } catch (e: Exception) {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    data = _gameDetailUiState.value.data.copy(
                        game = currentGame,
                    ),
                    error = OneTimeEvent(e),
                )
            }
        }
    }

    fun selectScreenshot(imageUrl: String) {
        _gameDetailUiState.value = _gameDetailUiState.value.copy(
            data = _gameDetailUiState.value.data.copy(
                selectedScreenshotUrl = imageUrl,
            ),
        )
    }

    fun play() {
        viewModelScope.launch {
            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                membersRepository.playGame(
                    resolvedMemberId,
                    gameId.toInt(),
                )
                    .onSuccess { play ->
                        _navigationEvent.value = OneTimeEvent(
                            PlayScreenArgs(
                                playUrl = play.playUrl,
                                gameName = _gameDetailUiState.value.data.game.title,
                            ),
                        )
                    }
                    .onFailure { e ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            error = OneTimeEvent(e),
                        )
                    }
            } catch (e: Exception) {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    error = OneTimeEvent(e),
                )
            }
        }
    }

    fun refreshMemberOnFocus() {
        viewModelScope.launch {
            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                membersRepository.getMember(resolvedMemberId)
                    .onFailure { e ->
                        _gameDetailUiState.value = _gameDetailUiState.value.copy(
                            error = OneTimeEvent(e)
                        )
                    }

            } catch (e: Exception) {
                _gameDetailUiState.value = _gameDetailUiState.value.copy(
                    error = OneTimeEvent(e)
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
    val id: Int,
    val title: String = "",
    val category: String = "",
    val thumbnailUrl: String = "",
)

@Immutable
data class PlayScreenArgs(
    val playUrl: String,
    val gameName: String,
)