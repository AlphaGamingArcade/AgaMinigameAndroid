package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.ui.utils.formatPlayerCount
import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.model.data.get

@Composable
fun HotGameCard(language: Language, game: Game, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp)),
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name.get(language.code),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            if (game.isTrending) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color(0xFFFF4500),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                        .size(16.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = game.name.get(language.code),
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (game.playerCount > 0) {
            Text(
                text = "${formatPlayerCount(game.playerCount)} playing",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 10.sp,
                maxLines = 1,
            )
        }
    }
}