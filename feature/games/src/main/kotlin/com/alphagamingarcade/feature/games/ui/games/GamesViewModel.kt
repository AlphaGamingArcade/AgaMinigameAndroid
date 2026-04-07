package com.alphagamingarcade.feature.games.ui.games

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.data.repository.home.HomeRepository
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Games view model.
 *
 * @param homeRepository [HomeRepository].
 */
@HiltViewModel
class GamesViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _gamesUiState = MutableStateFlow(UiState(GamesScreenData()))
    val gamesUiState = _gamesUiState
        .onStart { }
        .stateInDelayed(UiState(GamesScreenData()), viewModelScope)
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
    val bannerGames: List<Game> = listOf(
        Game(
            id = 101,
            name = "Neon Blaze",
            imageUrl = "https://picsum.photos/seed/neonblaze/800/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 102,
            name = "Golden Empire",
            imageUrl = "https://picsum.photos/seed/goldenempire/800/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 103,
            name = "Dragon's Vault",
            imageUrl = "https://picsum.photos/seed/dragonvault/800/400",
            isHot = false,
            isNew = true,
        ),
    ),
    val trendingGames: List<Game> = listOf(
        Game(
            id = 201,
            name = "Mystic Fortune",
            imageUrl = "https://picsum.photos/seed/mysticfortune/400/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 202,
            name = "Wild Safari",
            imageUrl = "https://picsum.photos/seed/wildsafari/400/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 203,
            name = "Pirate's Bounty",
            imageUrl = "https://picsum.photos/seed/piratesbounty/400/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 204,
            name = "Starfall",
            imageUrl = "https://picsum.photos/seed/starfall/400/400",
            isHot = false,
            isNew = false,
        ),
    ),
    val newReleases: List<Game> = listOf(
        Game(
            id = 301,
            name = "Cyber Reels",
            imageUrl = "https://picsum.photos/seed/cyberreels/400/400",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 302,
            name = "Moon Temple",
            imageUrl = "https://picsum.photos/seed/moontemple/400/400",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 303,
            name = "Ice Kingdom",
            imageUrl = "https://picsum.photos/seed/icekingdom/400/400",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 304,
            name = "Thunder Strike",
            imageUrl = "https://picsum.photos/seed/thunderstrike/400/400",
            isHot = true,
            isNew = true,
        ),
    ),
    val topRated: List<Game> = listOf(
        Game(
            id = 401,
            name = "Royal Flush",
            imageUrl = "https://picsum.photos/seed/royalflush/400/400",
            isHot = false,
            isNew = false,
        ),
        Game(
            id = 402,
            name = "Fortune Tiger",
            imageUrl = "https://picsum.photos/seed/fortunetiger/400/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 403,
            name = "Ocean Odyssey",
            imageUrl = "https://picsum.photos/seed/oceanodyssey/400/400",
            isHot = false,
            isNew = false,
        ),
        Game(
            id = 404,
            name = "Samurai Gold",
            imageUrl = "https://picsum.photos/seed/samuraigold/400/400",
            isHot = false,
            isNew = false,
        ),
    ),
    val jackpotGames: List<Game> = listOf(
        Game(
            id = 501,
            name = "Mega Millions",
            imageUrl = "https://picsum.photos/seed/megamillions/400/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 502,
            name = "Grand Jackpot",
            imageUrl = "https://picsum.photos/seed/grandjackpot/400/400",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
    ),
    val comingSoonGames: List<Game> = listOf(
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 503,
            name = "Cash Explosion",
            imageUrl = "https://picsum.photos/seed/cashexplosion/400/400",
            isHot = true,
            isNew = true,
        ),
    ),
)