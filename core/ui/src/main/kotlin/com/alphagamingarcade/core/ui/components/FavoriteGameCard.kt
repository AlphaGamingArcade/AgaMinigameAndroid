package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alphagamingarcade.model.data.Game
import java.util.Locale

@Composable
fun FavoriteGameCard(
    game: Game,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        // Thumbnail
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(game.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = game.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF0A0A0F).copy(alpha = 0.6f),
                            Color(0xFF0A0A0F).copy(alpha = 0.95f),
                        ),
                        startY = 60f
                    )
                )
        )

        // Top badges row
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (game.isTrending) {
                GameBadge(
                    label = "HOT",
                    containerColor = Color(0xFF7C1D1D),
                    contentColor = Color(0xFFFCA5A5),
                    icon = "🔥"
                )
            }
            if (game.isLatest) {
                GameBadge(
                    label = "NEW",
                    containerColor = Color(0xFF0F2D28),
                    contentColor = Color(0xFF6EE7B7),
                    icon = "✦"
                )
            }
        }

        // Favorite icon top-right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1A1A28).copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorited",
                tint = Color(0xFFF87171),
                modifier = Modifier.size(14.dp)
            )
        }

        // Bottom content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Category chip
            game.category.let {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF2D1F5E).copy(alpha = 0.9f),
                    border = BorderStroke(0.5.dp, Color(0xFF4C3A8A))
                ) {
                    Text(
                        text = it.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = Color(0xFFA78BFA),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            // Game name
            Text(
                text = game.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = (-0.2).sp
            )

            // Rating + Players row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Rating
                game.rating.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = String.format(Locale.ROOT, "%.1f", it),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFD4D4E8)
                        )
                    }
                }

                // Dot separator
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(Color(0xFF4A4A60), CircleShape)
                )

                // Player count
                game.playerCount.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF6B6B80),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = formatPlayerCount(it),
                            fontSize = 12.sp,
                            color = Color(0xFF6B6B80)
                        )
                    }
                }
            }
        }
    }
}

// ─── Badge ────────────────────────────────────────────────────────────────────

@Composable
private fun GameBadge(
    label: String,
    containerColor: Color,
    contentColor: Color,
    icon: String,
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = containerColor.copy(alpha = 0.9f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 9.sp)
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                color = contentColor
            )
        }
    }
}

// ─── Helper ───────────────────────────────────────────────────────────────────

private fun formatPlayerCount(count: Int): String = when {
    count >= 1_000_000 -> "${count / 1_000_000}M"
    count >= 1_000 -> "${count / 1_000}K"
    else -> count.toString()
}