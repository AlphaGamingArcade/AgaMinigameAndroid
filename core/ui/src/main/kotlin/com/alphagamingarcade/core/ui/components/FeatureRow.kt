package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.alphagamingarcade.model.data.Game

@Composable
public fun FeaturedRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = games, key = { it.id }) { game ->
            FeaturedGameCard(
                game = game,
                onClick = { onGameClick(game.id.toString()) }
            )
        }
    }
}
