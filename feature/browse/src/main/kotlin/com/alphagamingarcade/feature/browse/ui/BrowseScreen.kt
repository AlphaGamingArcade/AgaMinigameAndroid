package com.alphagamingarcade.feature.browse.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.components.FilterChips
import com.alphagamingarcade.core.ui.components.SearchBar
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.model.data.Game

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentBlue     = Color(0xFF2563EB)
private val TagNew         = Color(0xFF00C48C)
private val TagHot         = Color(0xFFFF4757)
private val SurfaceGray    = Color(0xFFF5F6FA)
private val SearchBarColor = Color(0xFFF0F1F5)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextSecondary  = Color(0xFF8A8A9A)

private val categories = listOf("All", "Hot", "New", "Trending", "Coming Soon")

@Composable
internal fun BrowseScreen(
    onGameClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    browseViewModel: BrowseViewModel = hiltViewModel(),
) {
    val browseState by browseViewModel.browseUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = browseState,
        onShowSnackbar = onShowSnackbar,
    ) { browseScreenData ->
        BrowseScreen(
            data = browseScreenData,
            onGameClick = onGameClick,
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun BrowseScreen(
    data: BrowseScreenData,
    onGameClick: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredGames = remember(data.allGames, selectedCategory, searchQuery) {
        data.allGames
            .filter { game ->
                when (selectedCategory) {
                    "Hot" -> game.isTrending
                    "New" -> game.isLatest
                    "Top" -> game.isTop
                    "Coming Soon" -> game.isComingSoon
                    else -> true
                }
            }
            .filter { game ->
                searchQuery.isBlank() || game.name.contains(searchQuery, ignoreCase = true)
            }
    }

    val isFiltering = searchQuery.isNotBlank() || selectedCategory != "All"

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            // ── Search Bar ───────────────────────────────────────────────────
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                )
            }

            // ── Category Chips ───────────────────────────────────────────────
            item {
                FilterChips(
                    categories = categories,
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it },
                )
                Spacer(Modifier.height(20.dp))
            }

            // ── Featured — only when not filtering ───────────────────────────
            if (!isFiltering && data.featuredGames.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Featured",
                        subtitle = "Editor's picks just for you",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    FeaturedRow(games = data.featuredGames, onGameClick = onGameClick)
                    Spacer(Modifier.height(24.dp))
                }
            }


            // ── All / Filtered Games Grid ─────────────────────────────────────
            item {
                SectionHeader(
                    title = if (!isFiltering) "All Games" else "Results (${filteredGames.size})",
                    subtitle = if (!isFiltering) "Browse the full collection" else "Showing filtered results",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                AllGamesGrid(
                    games = filteredGames,
                    onGameClick = onGameClick,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

// ─── Featured Row ─────────────────────────────────────────────────────────────

@Composable
private fun FeaturedRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onGameClick(game.id.toString()) },
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
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(0.6f)),
                            ),
                        ),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                ) {
                    if (game.isLatest) GameTag(label = "NEW", color = TagNew)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = game.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

// ─── Hot Games Row ────────────────────────────────────────────────────────────

@Composable
private fun HotGamesRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .clickable { onGameClick(game.id.toString()) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(14.dp)),
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
                            .padding(5.dp),
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = game.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ─── New Arrivals Row ─────────────────────────────────────────────────────────

@Composable
private fun NewArrivalsRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceGray)
                    .clickable { onGameClick(game.id.toString()) }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    GameTag(label = "NEW", color = TagNew)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = game.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "⭐ 4.7",
                        fontSize = 11.sp,
                        color = TextSecondary,
                    )
                }
            }
        }
    }
}

// ─── All Games Grid ───────────────────────────────────────────────────────────

@Composable
private fun AllGamesGrid(
    games: List<Game>,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridHeight = ((games.size + 1) / 2) * 180
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = modifier.height(gridHeight.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
    ) {
        items(games) { game ->
            GridGameCard(game = game, onClick = { onGameClick(game.id.toString()) })
        }
    }
}

@Composable
private fun GridGameCard(game: Game, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(14.dp)),
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (game.isTrending) GameTag(label = "HOT", color = TagHot)
                if (game.isLatest) GameTag(label = "NEW", color = TagNew)
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
            text = "⭐ 4.8 · Free to Play",
            fontSize = 11.sp,
            color = TextSecondary,
        )
    }
}

// ─── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary,
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = TextSecondary,
        )
    }
}

@Composable
private fun GameTag(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
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