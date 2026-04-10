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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

private val AccentBlue     = Color(0xFF2563EB)
private val TagNew         = Color(0xFF00C48C)
private val TagHot         = Color(0xFFFF4757)
private val SurfaceGray    = Color(0xFFF5F6FA)
private val SearchBarColor = Color(0xFFF0F1F5)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextSecondary  = Color(0xFF8A8A9A)

private val categories = listOf("All", "Hot", "New", "Slots", "Table")

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
                    "Hot" -> game.isHot
                    "New" -> game.isNew
                    "Slots" -> game.category == "Slots"
                    "Table" -> game.category == "Table"
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
                BrowseSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                )
            }

            // ── Category Chips ───────────────────────────────────────────────
            item {
                CategoryChips(
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

            // ── Hot Right Now — only when not filtering ───────────────────────
            if (!isFiltering && data.hotGames.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Hot Right Now",
                        subtitle = "Most played this hour",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    HotGamesRow(games = data.hotGames, onGameClick = onGameClick)
                    Spacer(Modifier.height(24.dp))
                }
            }

            // ── New Arrivals — only when not filtering ────────────────────────
            if (!isFiltering && data.newGames.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "New Releases",
                        subtitle = "Fresh drops this week",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    NewArrivalsRow(games = data.newGames, onGameClick = onGameClick)
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

// ─── Search Bar ──────────────────────────────────────────────────────────────

@Composable
private fun BrowseSearchBar(
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
                    Text(
                        text = "Search games...",
                        color = TextSecondary,
                        fontSize = 14.sp,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(fontSize = 14.sp, color = TextPrimary),
                    cursorBrush = SolidColor(AccentBlue),
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

// ─── Category Chips ──────────────────────────────────────────────────────────

@Composable
private fun CategoryChips(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selected,
                onClick = { onSelect(category) },
                label = {
                    Text(
                        text = category,
                        fontWeight = if (category == selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.onBackground,
                    selectedLabelColor = MaterialTheme.colorScheme.background,
                    containerColor = SurfaceGray,
                    labelColor = TextPrimary,
                ),
                border = null,
            )
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
                    if (game.isNew) GameTag(label = "NEW", color = TagNew)
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
        columns = GridCells.Fixed(2),
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
                if (game.isHot) GameTag(label = "HOT", color = TagHot)
                if (game.isNew) GameTag(label = "NEW", color = TagNew)
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