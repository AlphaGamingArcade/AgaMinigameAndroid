package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.model.data.Game

@Composable
fun HotGamesRow(language: Language, games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(games, key = { it.id }) { game ->
            HotGameCard (language = language, game = game, onClick = { onGameClick(game.id.toString()) })
        }
    }
}