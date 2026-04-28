package com.alphagamingarcade.feature.favorite.ui.favorite

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.MembersRepository
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val membersRepository: MembersRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _favoriteUiState = MutableStateFlow(UiState(FavoriteScreenData()))
    val homeUiState = _favoriteUiState
        .onStart {}
        .stateInDelayed(UiState(FavoriteScreenData()), viewModelScope)

    init {
        loadMemberRecentPlayed()
        loadMemberFavorites()
    }

    private fun loadMemberFavorites() {
        viewModelScope.launch {
            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                val result = membersRepository.getMemberFavorites(
                    memberId = resolvedMemberId,
                    pageNumber = 1,
                    pageSize = 10
                )

                result.onSuccess { games ->
                    val currentData = _favoriteUiState.value.data
                    _favoriteUiState.value = UiState(
                        data = currentData.copy(games = games)
                    )
                }.onFailure { e ->
                    _favoriteUiState.value = UiState(
                        data = FavoriteScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
            } catch (e: Exception) {
                _favoriteUiState.value = UiState(
                    data = FavoriteScreenData(),
                    error = OneTimeEvent(e)
                )
            }
        }
    }

    private fun loadMemberRecentPlayed() {
        viewModelScope.launch {
            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                val result = membersRepository.getMemberRecentPlayed(
                    memberId = resolvedMemberId,
                    pageNumber = 1,
                    pageSize = 10
                )

                result.onSuccess { games ->
                    val currentData = _favoriteUiState.value.data
                    _favoriteUiState.value = UiState(
                        data = currentData.copy(recentGames = games)
                    )
                }.onFailure { e ->
                    _favoriteUiState.value = UiState(
                        data = FavoriteScreenData(),
                        error = OneTimeEvent(e)
                    )
                }
            } catch (e: Exception) {
                _favoriteUiState.value = UiState(
                    data = FavoriteScreenData(),
                    error = OneTimeEvent(e)
                )
            }
        }
    }

    fun removeFavorite(gameId: Int) {
        viewModelScope.launch {
            val currentData = _favoriteUiState.value.data

            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                val result = membersRepository.removeFavorite(
                    memberId = resolvedMemberId,
                    gameId = gameId,
                )

                result.onSuccess {
                    _favoriteUiState.value = UiState(
                        data = currentData.copy(
                            games = currentData.games.filter { it.id.toInt() != gameId }
                        ),
                    )
                }.onFailure { e ->
                    _favoriteUiState.value = UiState(
                        data = currentData,
                        error = OneTimeEvent(e),
                    )
                }
            } catch (e: Exception) {
                _favoriteUiState.value = UiState(
                    data = currentData,
                    error = OneTimeEvent(e),
                )
            }
        }
    }
}

/**
 * Favorite screen data.
 *
 * @param games      Full list of favorited games.
 * @param recentGames Most recently played favorite game.
 */
@Immutable
data class FavoriteScreenData(
    val games: List<Game> = emptyList(),
    val recentGames: List<Game>  = emptyList()
)