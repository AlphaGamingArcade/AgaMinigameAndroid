package com.alphagamingarcade.feature.games.ui.categories

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.data.repository.home.HomeRepository
import com.alphagamingarcade.model.data.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val categoryName: String = savedStateHandle.get<String>("categoryName") ?: ""

    private val _uiState = MutableStateFlow(UiState(CategoriesScreenData()))
    val uiState = _uiState
        .onStart { loadGames() }
        .stateInDelayed(UiState(CategoriesScreenData()), viewModelScope)

    private fun loadGames() {
        val isComingSoon = categoryName in listOf("VIP", "Sports")
        val dummyGames = if (isComingSoon) emptyList() else getDummyGamesForCategory(categoryName)

        viewModelScope.launch {
            // Simulate loading delay
            delay(800)
            _uiState.value = UiState(
                CategoriesScreenData(
                    games = dummyGames,
                    isComingSoon = isComingSoon,
                    isLoading = false, // ← set to false after loading
                ),
            )
        }
    }

    private fun getDummyGamesForCategory(category: String): List<Game> = when (category) {
        "Slots" -> listOf(
            Game(id = 1, name = "Neon Blaze", imageUrl = "https://picsum.photos/seed/slot1/400/400", isHot = true, isNew = false),
            Game(id = 2, name = "Golden Empire", imageUrl = "https://picsum.photos/seed/slot2/400/400", isHot = false, isNew = true),
            Game(id = 3, name = "Dragon's Vault", imageUrl = "https://picsum.photos/seed/slot3/400/400", isHot = true, isNew = true),
            Game(id = 4, name = "Lucky Sevens", imageUrl = "https://picsum.photos/seed/slot4/400/400", isHot = false, isNew = false),
            Game(id = 5, name = "Mystic Reels", imageUrl = "https://picsum.photos/seed/slot5/400/400", isHot = true, isNew = false),
            Game(id = 6, name = "Wild Diamonds", imageUrl = "https://picsum.photos/seed/slot6/400/400", isHot = false, isNew = true),
        )
        "Table" -> listOf(
            Game(id = 7, name = "Blackjack Pro", imageUrl = "https://picsum.photos/seed/table1/400/400", isHot = true, isNew = false),
            Game(id = 8, name = "Roulette Royale", imageUrl = "https://picsum.photos/seed/table2/400/400", isHot = false, isNew = false),
            Game(id = 9, name = "Baccarat Classic", imageUrl = "https://picsum.photos/seed/table3/400/400", isHot = true, isNew = true),
            Game(id = 10, name = "Poker Masters", imageUrl = "https://picsum.photos/seed/table4/400/400", isHot = false, isNew = false),
        )
        "Live" -> listOf(
            Game(id = 11, name = "Live Blackjack", imageUrl = "https://picsum.photos/seed/live1/400/400", isHot = true, isNew = false),
            Game(id = 12, name = "Live Roulette", imageUrl = "https://picsum.photos/seed/live2/400/400", isHot = true, isNew = false),
            Game(id = 13, name = "Live Baccarat", imageUrl = "https://picsum.photos/seed/live3/400/400", isHot = false, isNew = true),
            Game(id = 14, name = "Dream Catcher", imageUrl = "https://picsum.photos/seed/live4/400/400", isHot = false, isNew = false),
            Game(id = 15, name = "Crazy Time", imageUrl = "https://picsum.photos/seed/live5/400/400", isHot = true, isNew = true),
        )
        "Arcade" -> listOf(
            Game(id = 16, name = "Plinko Rush", imageUrl = "https://picsum.photos/seed/arcade1/400/400", isHot = false, isNew = true),
            Game(id = 17, name = "Mines Blast", imageUrl = "https://picsum.photos/seed/arcade2/400/400", isHot = true, isNew = false),
            Game(id = 18, name = "Crash Rocket", imageUrl = "https://picsum.photos/seed/arcade3/400/400", isHot = true, isNew = true),
            Game(id = 19, name = "Keno Classic", imageUrl = "https://picsum.photos/seed/arcade4/400/400", isHot = false, isNew = false),
        )
        else -> listOf(
            Game(id = 20, name = "Mystery Game 1", imageUrl = "https://picsum.photos/seed/misc1/400/400", isHot = false, isNew = true),
            Game(id = 21, name = "Mystery Game 2", imageUrl = "https://picsum.photos/seed/misc2/400/400", isHot = true, isNew = false),
        )
    }
}

@Immutable
data class CategoriesScreenData(
    val games: List<Game> = emptyList(),
    val isComingSoon: Boolean = false,
    val isLoading: Boolean = true, // ← add this
)