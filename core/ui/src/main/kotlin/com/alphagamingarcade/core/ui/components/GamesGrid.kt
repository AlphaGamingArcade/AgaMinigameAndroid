package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.model.data.Game

@Composable
fun GamesGrid(
    language: Language,
    games: List<Game>,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 3,
    spacing: Dp = 10.dp,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        games.chunked(columns).forEach { rowGames ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                rowGames.forEach { game ->
                    GameItem(
                        language = language,
                        game = game,
                        modifier = Modifier.weight(1f),
                        onPlayClick = { onGameClick(game.id.toString()) },
                    )
                }
                // Pad incomplete last row so items don't stretch
                repeat(columns - rowGames.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}