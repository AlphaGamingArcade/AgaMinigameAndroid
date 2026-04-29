package com.alphagamingarcade.feature.games.ui.games

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.BannersRepository
import com.alphagamingarcade.core.data.repository.GamesRepository
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.Banner
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
class GamesViewModel @Inject constructor(
    private val bannersRepository: BannersRepository,
    private val gamesRepository: GamesRepository
) : ViewModel() {
    private val _gamesUiState = MutableStateFlow(UiState(GamesScreenData()))
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val gamesUiState = _gamesUiState
        .onStart {
            loadBanners()
            loadTrendingGames()
            loadLatestGames()
            loadTopGames()
            loadComingSoonGames()
        }
        .stateInDelayed(UiState(GamesScreenData()), viewModelScope)

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                loadBanners()
                loadTrendingGames()
                loadLatestGames()
                loadTopGames()
                loadComingSoonGames()// re-fetch your data here
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadBanners() {
        viewModelScope.launch {
            bannersRepository.getBanners()
                .catch { e ->
                    _gamesUiState.value = UiState(
                        data = GamesScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { banners ->
                    val currentData = _gamesUiState.value.data
                    _gamesUiState.value = UiState(
                        data = currentData.copy(bannerGames = banners)
                    )
                }
        }
    }

    private fun loadTrendingGames() {
        viewModelScope.launch {
            gamesRepository.getTrendingGames()
                .catch { e ->

                    _gamesUiState.value = UiState(
                        data = GamesScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    val currentData = _gamesUiState.value.data
                    _gamesUiState.value = UiState(
                        data = currentData.copy(trendingGames = games)
                    )
                }
        }
    }

    private fun loadLatestGames() {
        viewModelScope.launch {
            gamesRepository.getLatestGames()
                .catch { e ->

                    _gamesUiState.value = UiState(
                        data = GamesScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    val currentData = _gamesUiState.value.data
                    _gamesUiState.value = UiState(
                        data = currentData.copy(newReleases = games)
                    )
                }
        }
    }

    private fun loadTopGames() {
        viewModelScope.launch {
            gamesRepository.getTopGames()
                .catch { e ->

                    _gamesUiState.value = UiState(
                        data = GamesScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    val currentData = _gamesUiState.value.data
                    _gamesUiState.value = UiState(
                        data = currentData.copy(topRated = games)
                    )
                }
        }
    }

    private fun loadComingSoonGames() {
        viewModelScope.launch {
            gamesRepository.getComingSoonGames()
                .catch { e ->

                    _gamesUiState.value = UiState(
                        data = GamesScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
                .collect { games ->
                    val currentData = _gamesUiState.value.data
                    _gamesUiState.value = UiState(
                        data = currentData.copy(comingSoonGames = games)
                    )
                }
        }
    }
}

/**
 * Games screen data.
 *
 * @param bannerGames   Games shown in the hero banner carousel.
 * @param trendingGames Games that are currently trending.
 * @param newReleases   Recently released games.
 * @param topRated      Highest-rated games.
 * @param jackpotGames  Games with active jackpots.
 */
@Immutable
data class GamesScreenData(
    val bannerGames: List<Banner> = emptyList(),
    val trendingGames: List<Game> = emptyList(),
    val newReleases: List<Game> = emptyList(),
    val topRated: List<Game> = emptyList(),
    val jackpotGames: List<Game> = listOf(
        Game(
            id = 501,
            name = "Mega Millions",
            imageUrl = "https://picsum.photos/seed/megamillions/400/400",
            isTrending = true,
            isLatest = false,
        )
    ),
    val comingSoonGames: List<Game> = emptyList()
)