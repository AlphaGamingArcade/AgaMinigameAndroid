package com.alphagamingarcade.feature.browse.ui

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
 * Browse view model.
 *
 * @param homeRepository [HomeRepository].
 */
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _browseUiState = MutableStateFlow(UiState(BrowseScreenData()))
    val browseUiState = _browseUiState
        .onStart { }
        .stateInDelayed(UiState(BrowseScreenData()), viewModelScope)
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
    val allGames: List<Game> = listOf(
        Game(
            id = 1,
            name = "Snow Peak",
            imageUrl = "https://fastly.picsum.photos/id/786/400/400.jpg?hmac=-i28je2Fi45DID1aEqglQHJ1VpLE-UUwamtmczeUkbs",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 2,
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 3,
            name = "Neon Rush",
            imageUrl = "https://fastly.picsum.photos/id/392/400/400.jpg?hmac=Gzjp3oBMWRgSXg5Rqe5yT1LlSz0YkIjxG1vKVH7T0k",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 4,
            name = "Dragon Clash",
            imageUrl = "https://fastly.picsum.photos/id/433/400/400.jpg?hmac=R7jy6RI6GvJ0W4sEFAh-LKxrDy5xkABLvEy3YgCEb2M",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 5,
            name = "Golden Spin",
            imageUrl = "https://fastly.picsum.photos/id/503/400/400.jpg?hmac=l5eDUoHHlqVoA2PrjJ3pFKKvhRdvUHmIF9V2Z5XGZdI",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 6,
            name = "Wild Jungle",
            imageUrl = "https://fastly.picsum.photos/id/582/400/400.jpg?hmac=V6Hf3fLnCXdO6hLm3xY_h5uPbYKvZZcPDzH5q-Jkq6Y",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 7,
            name = "Sky Pirates",
            imageUrl = "https://fastly.picsum.photos/id/614/400/400.jpg?hmac=7vA3H6zWJPOJXC2ZhK5VgFgTfFYfCHq9JdOHxC9RnMY",
            isHot = false,
            isNew = false,
        ),
        Game(
            id = 8,
            name = "Ice Queen",
            imageUrl = "https://fastly.picsum.photos/id/659/400/400.jpg?hmac=GqE-7HJLS1w5Kh7bLjE_Ap1hqOhgVf3iRgbJEy_gxQ0",
            isHot = false,
            isNew = true,
        ),
        Game(
            id = 9,
            name = "Thunder God",
            imageUrl = "https://fastly.picsum.photos/id/669/400/400.jpg?hmac=dUMVLElNWI7v5mHbMMh-LmJl5Z-QhC8y5K6m0fMFvV8",
            isHot = true,
            isNew = true,
        ),
        Game(
            id = 10,
            name = "Ocean Depths",
            imageUrl = "https://fastly.picsum.photos/id/777/400/400.jpg?hmac=oxZMPVNDjMpGS5-C0KYOPYWh0CiWO5x1y3kLzGbV5lM",
            isHot = false,
            isNew = false,
        ),
        Game(
            id = 11,
            name = "Samurai Way",
            imageUrl = "https://fastly.picsum.photos/id/811/400/400.jpg?hmac=YT3vGQkRJrQP5GjYpBHnzSmktmA00eiM82gX5bCkMqI",
            isHot = true,
            isNew = false,
        ),
        Game(
            id = 12,
            name = "Fortune Wheel",
            imageUrl = "https://fastly.picsum.photos/id/870/400/400.jpg?hmac=TNnA-S4Bs_FSmX-3BUGk9hMQIL5B3JxJByPXhZcPEIM",
            isHot = false,
            isNew = true,
        ),
    ),
) {
    val featuredGames get() = allGames.take(5)
    val hotGames get() = allGames.filter { it.isHot }
    val newGames get() = allGames.filter { it.isNew }
}