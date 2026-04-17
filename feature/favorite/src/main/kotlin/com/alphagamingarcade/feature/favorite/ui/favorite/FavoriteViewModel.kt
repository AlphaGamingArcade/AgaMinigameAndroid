package com.alphagamingarcade.feature.favorite.ui.favorite

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor() : ViewModel() {
    private val _favoriteUiState = MutableStateFlow(UiState(FavoriteScreenData()))
    val homeUiState = _favoriteUiState
        .onStart { }
        .stateInDelayed(UiState(FavoriteScreenData()), viewModelScope)
}

/**
 * Favorite screen data.
 *
 * @param games      Full list of favorited games.
 * @param recentGame Most recently played favorite game.
 */
@Immutable
data class FavoriteScreenData(
    val games: List<Game> = listOf(
        Game(
            id = "1",
            name = "Snow Peak",
            imageUrl = "https://fastly.picsum.photos/id/786/400/400.jpg?hmac=-i28je2Fi45DID1aEqglQHJ1VpLE-UUwamtmczeUkbs",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = "2",
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = "3",
            name = "Neon Rush",
            imageUrl = "https://fastly.picsum.photos/id/392/400/400.jpg?hmac=Gzjp3oBMWRgSXg5Rqe5yT1LlSz0YkIjxG1vKVH7T0k",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = "4",
            name = "Dragon Clash",
            imageUrl = "https://fastly.picsum.photos/id/433/400/400.jpg?hmac=R7jy6RI6GvJ0W4sEFAh-LKxrDy5xkABLvEy3YgCEb2M",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = "5",
            name = "Golden Spin",
            imageUrl = "https://fastly.picsum.photos/id/503/400/400.jpg?hmac=l5eDUoHHlqVoA2PrjJ3pFKKvhRdvUHmIF9V2Z5XGZdI",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = "6",
            name = "Wild Jungle",
            imageUrl = "https://fastly.picsum.photos/id/582/400/400.jpg?hmac=V6Hf3fLnCXdO6hLm3xY_h5uPbYKvZZcPDzH5q-Jkq6Y",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = "7",
            name = "Sky Pirates",
            imageUrl = "https://fastly.picsum.photos/id/614/400/400.jpg?hmac=7vA3H6zWJPOJXC2ZhK5VgFgTfFYfCHq9JdOHxC9RnMY",
            isHot = false,
            isNew = false,
        ),
        Game(
            id = "8",
            name = "Ice Queen",
            imageUrl = "https://fastly.picsum.photos/id/659/400/400.jpg?hmac=GqE-7HJLS1w5Kh7bLjE_Ap1hqOhgVf3iRgbJEy_gxQ0",
            isHot = false,
            isNew = true,
        ),
    ),
) {
    val recentGame get() = games.firstOrNull()
    val hotFavorites get() = games.filter { it.isHot }
}