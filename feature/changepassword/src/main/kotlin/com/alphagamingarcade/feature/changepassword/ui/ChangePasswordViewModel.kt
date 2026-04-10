package com.alphagamingarcade.feature.changepassword.ui

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
class ChangePasswordViewModel @Inject constructor() : ViewModel() {
    private val _changePasswordUiState = MutableStateFlow(UiState(ChangePasswordScreenData()))
    val browseUiState = _changePasswordUiState
        .onStart { }
        .stateInDelayed(UiState(ChangePasswordScreenData()), viewModelScope)
}
@Immutable
data class ChangePasswordScreenData(
    val games: List<Game> = listOf(
        Game(
            id = 5,
            name = "Snow Peak",
            imageUrl = "https://fastly.picsum.photos/id/786/400/400.jpg?hmac=-i28je2Fi45DID1aEqglQHJ1VpLE-UUwamtmczeUkbs",
            isHot = true,
            isNew = true
        ),
        Game(
            id = 6,
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true
        ),
        Game(
            id = 7,
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true
        ),
        Game(
            id = 8,
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true
        ),
        Game(
            id = 9,
            name = "Desert Rally",
            imageUrl = "https://fastly.picsum.photos/id/175/400/400.jpg?hmac=3R0ZitObzzLehq0hkXQfNWbf5fw7aHArfw8OxdKsPZI",
            isHot = false,
            isNew = true
        )
    ),
)