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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alphagamingarcade.core.data.model.Language
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.feature.favorite.R
import com.alphagamingarcade.core.ui.components.SearchBar
import com.alphagamingarcade.model.data.get

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentPink     = Color(0xFFE91E8C)
private val TagNew         = Color(0xFF00C48C)
private val TagHot         = Color(0xFFFF4757)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextSecondary  = Color(0xFF8A8A9A)

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun FavoriteScreen(
    onResumeGameClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    browseViewModel: FavoriteViewModel = hiltViewModel(),
) {
    val homeState by browseViewModel.homeUiState.collectAsStateWithLifecycle()
    val isRefreshing by browseViewModel.isRefreshing.collectAsStateWithLifecycle()
    val language by browseViewModel.language.collectAsStateWithLifecycle()

    StatefulComposable(
        state = homeState,
        onShowSnackbar = onShowSnackbar,
    ) { homeScreenData ->
        FavoriteScreen(
            language = language,
            data = homeScreenData,
            isRefreshing = isRefreshing,
            onRefresh = browseViewModel::refresh,
            onResumeGameClick = onResumeGameClick,
            onRemoveFavoriteClick = { game ->
                browseViewModel.removeFavorite(game.id.toInt())
            }
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun FavoriteScreen(
    language: Language,
    data: FavoriteScreenData,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onResumeGameClick: (String) -> Unit,
    onRemoveFavoriteClick: (Game) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGameToRemove by remember { mutableStateOf<Game?>(null) }

    val filteredGames = remember(searchQuery, data.games) {
        if (searchQuery.isBlank()) data.games
        else data.games.filter { it.name.get(language.code).contains(searchQuery, ignoreCase = true) }
    }

    val isSearching = searchQuery.isNotBlank()

    selectedGameToRemove?.let { game ->
        AlertDialog(
            onDismissRequest = {
                selectedGameToRemove = null
            },
            title = { Text(stringResource(R.string.remove_from_favorites)) },
            text = { Text(stringResource(R.string.remove_from_favorites, game.name)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedGameToRemove = null
                        onRemoveFavoriteClick(game)
                    }
                ) {
                    Text(stringResource(R.string.remove))
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedGameToRemove = null }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            onRefresh = onRefresh,
            isRefreshing = isRefreshing
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
            ) {
                // ── Search Bar ───────────────────────────────────────────────────
                item {
                    SearchBar(
                        placeholder = stringResource(R.string.search_favorites),
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
                    data.recentGames.firstOrNull()?.let { recent ->
                        item {
                            SectionLabel(
                                title = stringResource(R.string.continue_playing),
                                modifier = Modifier.padding(horizontal = 20.dp),
                            )
                            Spacer(Modifier.height(10.dp))
                            ResumeCard(
                                language = language,
                                game = recent,
                                onClick = { onResumeGameClick(recent.id.toString()) },
                                modifier = Modifier.padding(horizontal = 20.dp),
                            )
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }

//            if (!isSearching && data.recentGames.isNotEmpty()) {
//                item {
//                    SectionLabel(
//                        title = "Continue Playing",
//                        modifier = Modifier.padding(horizontal = 20.dp),
//                    )
//                    Spacer(Modifier.height(10.dp))
//                }
//                items(data.recentGames) { recent ->
//                    ResumeCard(
//                        game = recent,
//                        onClick = { onGameClick(recent.id.toString()) },
//                        modifier = Modifier.padding(horizontal = 20.dp),
//                    )
//                    Spacer(Modifier.height(10.dp))
//                }
//                item {
//                    Spacer(Modifier.height(14.dp))
//                }
//            }

                // ── All Favorites grid ────────────────────────────────────────────
                item {
                    SectionLabel(
                        title = if (isSearching) "${stringResource(R.string.results)} (${filteredGames.size})" else stringResource(R.string.all_favorites),
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
                            language = language,
                            games = filteredGames,
                            onGameClick = onResumeGameClick,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            onRemoveFavoriteClick = { game ->
                                selectedGameToRemove = game
                            }
                        )
                    }
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


// ─── Resume Card ─────────────────────────────────────────────────────────────

@Composable
private fun ResumeCard(
    language: Language,
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
            contentDescription = game.name.get(language.code),
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
                text = stringResource(R.string.last_played),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = game.name.get(language.code),
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
                    text = "▶  ${stringResource(R.string.resume)}",
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
private fun HotFavoritesRow(language: Language, games: List<Game>, onGameClick: (String) -> Unit) {
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
                        contentDescription = game.name.get(language.code),
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
                    text = game.name.get(language.code),
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
    language: Language,
    games: List<Game>,
    onGameClick: (String) -> Unit,
    onRemoveFavoriteClick: (Game) -> Unit,
    modifier: Modifier = Modifier
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
                language = language,
                game = game,
                onGameClick = { onGameClick(game.id.toString()) },
                onRemoveFavoriteClick = { onRemoveFavoriteClick(game) }
            )
        }
    }
}

@Composable
private fun FavoriteGridCard(
    language: Language,
    game: Game,
    onGameClick: () -> Unit,
    onRemoveFavoriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onGameClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name.get(language.code),
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

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .clickable(onClick = onRemoveFavoriteClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = AccentPink,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = game.name.get(language.code),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = stringResource(R.string.favorited),
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp)
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
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isSearching) stringResource(R.string.no_results_found) else stringResource(R.string.no_favorites),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = if (isSearching) stringResource(R.string.no_results_found_sub_title)
            else stringResource(R.string.no_favorites_sub_title),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        color = MaterialTheme.colorScheme.onSurface,
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