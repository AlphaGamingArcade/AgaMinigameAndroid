package com.alphagamingarcade.feature.favorite.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.model.data.Game

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentPink     = Color(0xFFE91E8C)
private val TagNew         = Color(0xFF00C48C)
private val TagHot         = Color(0xFFFF4757)
private val SurfaceGray    = Color(0xFFF5F6FA)
private val SearchBarColor = Color(0xFFF0F1F5)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextSecondary  = Color(0xFF8A8A9A)

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun FavoriteScreen(
    onGameClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    browseViewModel: FavoriteViewModel = hiltViewModel(),
) {
    val homeState by browseViewModel.homeUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = homeState,
        onShowSnackbar = onShowSnackbar,
    ) { homeScreenData ->
        FavoriteScreen(
            data = homeScreenData,
            onGameClick = onGameClick,
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun FavoriteScreen(
    data: FavoriteScreenData,
    onGameClick: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredGames = remember(searchQuery, data.games) {
        if (searchQuery.isBlank()) data.games
        else data.games.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val isSearching = searchQuery.isNotBlank()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            // ── Search Bar ───────────────────────────────────────────────────
            item {
                FavoriteSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Resume Playing card — only when not searching ─────────────────
            if (!isSearching) {
                data.recentGame?.let { recent ->
                    item {
                        SectionLabel(
                            title = "Continue Playing",
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        Spacer(Modifier.height(10.dp))
                        ResumeCard(
                            game = recent,
                            onClick = { onGameClick(recent.id.toString()) },
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }

            // ── Hot Favorites — only when not searching ───────────────────────
            if (!isSearching && data.hotFavorites.isNotEmpty()) {
                item {
                    SectionLabel(
                        title = "🔥 Hot Favorites",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(10.dp))
                    HotFavoritesRow(
                        games = data.hotFavorites,
                        onGameClick = onGameClick,
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }

            // ── All Favorites grid ────────────────────────────────────────────
            item {
                SectionLabel(
                    title = if (isSearching) "Results (${filteredGames.size})" else "All Favorites",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(10.dp))
            }

            if (filteredGames.isEmpty()) {
                item {
                    FavoriteEmptyState(isSearching = isSearching)
                }
            } else {
                item {
                    FavoritesGrid(
                        games = filteredGames,
                        onGameClick = onGameClick,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }
    }
}

// ─── Header ──────────────────────────────────────────────────────────────────
//
//@Composable
//private fun FavoriteHeader(count: Int) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp, vertical = 20.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//    ) {
//        Column {
//            Text(
//                text = "Favorites",
//                fontWeight = FontWeight.ExtraBold,
//                fontSize = 26.sp,
//                color = TextPrimary,
//            )
//            Text(
//                text = "$count games saved",
//                fontSize = 13.sp,
//                color = TextSecondary,
//            )
//        }
//        // Heart badge
//        Box(
//            modifier = Modifier
//                .size(48.dp)
//                .clip(RoundedCornerShape(14.dp))
//                .background(
//                    Brush.linearGradient(
//                        colors = listOf(AccentPink, AccentRose),
//                    ),
//                ),
//            contentAlignment = Alignment.Center,
//        ) {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(22.dp),
//            )
//        }
//    }
//}

// ─── Search Bar ──────────────────────────────────────────────────────────────

@Composable
private fun FavoriteSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SearchBarColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(text = "Search favorites...", color = TextSecondary, fontSize = 14.sp)
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary),
                    cursorBrush = SolidColor(AccentPink),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(20.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

// ─── Resume Card ─────────────────────────────────────────────────────────────

@Composable
private fun ResumeCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = game.imageUrl,
            contentDescription = game.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Black.copy(0.75f), Color.Transparent),
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(20.dp),
        ) {
            Text(
                text = "Last Played",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = game.name,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccentPink)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            ) {
                Text(
                    text = "▶  Resume",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// ─── Hot Favorites Row ────────────────────────────────────────────────────────

@Composable
private fun HotFavoritesRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            Column(
                modifier = Modifier
                    .width(110.dp)
                    .clickable { onGameClick(game.id.toString()) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp)),
                ) {
                    AsyncImage(
                        model = game.imageUrl,
                        contentDescription = game.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    GameTag(
                        label = "HOT",
                        color = TagHot,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp),
                    )
                    // Favorite heart
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AccentPink,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = game.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ─── Favorites Grid ───────────────────────────────────────────────────────────

@Composable
private fun FavoritesGrid(
    games: List<Game>,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridHeight = ((games.size + 1) / 2) * 190
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.height(gridHeight.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
    ) {
        items(items = games, key = { it.id }) { game ->
            FavoriteGridCard(
                game = game,
                onClick = { onGameClick(game.id.toString()) },
            )
        }
    }
}

@Composable
private fun FavoriteGridCard(game: Game, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            // Tags row
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (game.isHot) GameTag(label = "HOT", color = TagHot)
                if (game.isNew) GameTag(label = "NEW", color = TagNew)
            }
            // Favorite heart overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = AccentPink,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = game.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "⭐ 4.8 · Favorited",
            fontSize = 11.sp,
            color = TextSecondary,
        )
    }
}

// ─── Empty State ─────────────────────────────────────────────────────────────

@Composable
private fun FavoriteEmptyState(isSearching: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceGray),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFE0D0E8),
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isSearching) "No results found" else "No favorites yet",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
        )
        Text(
            text = if (isSearching) "Try a different search term"
            else "Games you favorite will appear here",
            fontSize = 13.sp,
            color = TextSecondary,
        )
    }
}

// ─── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = TextPrimary,
        modifier = modifier,
    )
}

@Composable
private fun GameTag(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
        )
    }
}