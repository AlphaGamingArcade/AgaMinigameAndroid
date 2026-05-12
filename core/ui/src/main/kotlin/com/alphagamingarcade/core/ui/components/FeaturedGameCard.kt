package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun FeaturedGameCard(language: Language, game: Game, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = game.imageUrl,
            contentDescription = game.name.get(language.code),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 40f,
                    )
                ),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (game.isTrending) GameBadge(label = "HOT", color = Color(0xFFFF4500))
                if (game.isLatest) GameBadge(label = "NEW", color = Color(0xFF00C853))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = game.name.get(language.code),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Rating row
            if (game.rating > 0f) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(11.dp),
                    )
                    Text(
                        text = "%.1f".format(game.rating),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                    )
                    if (game.playerCount > 0) {
                        Text(
                            text = "· ${formatPlayerCount(game.playerCount)} playing",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                        )
                    }
                }
            }
        }
    }
}