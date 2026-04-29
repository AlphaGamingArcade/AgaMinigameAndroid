package com.alphagamingarcade.feature.games.ui.categories

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import com.alphagamingarcade.feature.games.R

enum class CategoryFilter(
    @StringRes val labelRes: Int,
) {
    All(R.string.all_filter),
    Popular(R.string.popular_filter),
    New(R.string.new_filter),
    TopRated(R.string.top_rated_filter),
}

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun CategoriesScreen(
    categoryName: String,
    onGameClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    viewModel: CategoriesViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    LaunchedEffect(categoryName) {
        viewModel.initialize(categoryName)
    }

    StatefulComposable(
        state = state,
        onShowSnackbar = onShowSnackbar,
    ) { data ->
        CategoriesScreen(
            data = data,
            onRefresh = viewModel::refresh,
            isRefreshing = isRefreshing,
            categoryName = categoryName,
            onGameClick = onGameClick,
            onBackClick = onBackClick,
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriesScreen(
    categoryName: String,
    data: CategoriesScreenData,
    onGameClick: (String) -> Unit,
    onBackClick: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(CategoryFilter.All) }

    val filteredGames = remember(searchQuery, selectedFilter, data.games) {
        data.games
            .filter { game ->
                searchQuery.isEmpty() || game.name.contains(searchQuery, ignoreCase = true)
            }
            .let { games ->
                when (selectedFilter) {
                    CategoryFilter.All -> games
                    CategoryFilter.New -> games.filter { it.isLatest }
                    CategoryFilter.Popular -> games.filter { it.isTrending }
                    CategoryFilter.TopRated -> games.sortedByDescending { it.id }
                }
            }
    }

    val filterLabels = CategoryFilter.entries.map { filter ->
        filter to stringResource(id = filter.labelRes)
    }


    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Top App Bar ──────────────────────────────────────────────────
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    title = {
                        Text(
                            text = categoryName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1A1A2E),
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        navigationIconContentColor = Color(0xFF1A1A2E),
                    ),
                )

                PullToRefreshBox(
                    modifier = Modifier.fillMaxSize(),
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                ){
                    if (data.isLoading) {
                        // ── Shimmer Loading State ────────────────────────────────────
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                            userScrollEnabled = false, // disable scroll during loading
                        ) {
                            // Shimmer search bar
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(shimmerBrush()),
                                )
                            }
                            // Shimmer filter chips
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ShimmerFilterChips()
                            }
                            // Shimmer game cards — 6 placeholders
                            items(6) { index ->
                                ShimmerGameCard()
                            }
                        }
                    } else {
                        // ── Actual Content ───────────────────────────────────────────
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                SearchBar(
                                    query = searchQuery,
                                    onQueryChange = { searchQuery = it },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                )
                            }
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                FilterChips(
                                    categories = filterLabels.map { it.second },
                                    selected = stringResource(id = selectedFilter.labelRes),
                                    onSelect = { selectedLabel ->
                                        selectedFilter = filterLabels.first { it.second == selectedLabel }.first
                                    },
                                )
                            }
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = "${filteredGames.size} Game${if (filteredGames.size != 1) "s" else ""} Available",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF8A8A9A)
                                )
                            }
                            if (filteredGames.isEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    EmptyState(isSearching = searchQuery.isNotEmpty())
                                }
                            } else {
                                itemsIndexed(items = filteredGames, key = { _, game -> game.id }) { index, game ->
                                    CategoryGameCard(
                                        game = game,
                                        onClick = { onGameClick(game.id.toString()) }
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }
}


// ─── Category Game Card ───────────────────────────────────────────────────────

@Composable
private fun CategoryGameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F6FA)),
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
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (game.isLatest) GameTag(label = "NEW", color = Color(0xFF00C48C))
                if (game.isTrending) GameTag(label = "HOT", color = Color(0xFFFF4757))
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = game.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color(0xFF1A1A2E),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Text(
            text = "⭐ 4.8",
            fontSize = 11.sp,
            color = Color(0xFF8A8A9A),
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Spacer(Modifier.height(4.dp))
    }
}

// ─── Empty State ─────────────────────────────────────────────────────────────

@Composable
private fun EmptyState(isSearching: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = if (isSearching) Icons.Rounded.Search else Icons.Rounded.SportsEsports,
                contentDescription = null,
                tint = Color(0xFF0A3535),
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = if (isSearching) "No results found" else "No games yet",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A1A2E),
            )
            Text(
                text = if (isSearching) "Try a different search term" else "Check back later!",
                fontSize = 13.sp,
                color = Color(0xFF8A8A9A),
            )
        }
    }
}

// ─── Game Tag ─────────────────────────────────────────────────────────────────

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

// ─── Shimmer Effect ───────────────────────────────────────────────────────────

@Composable
private fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color(0xFFE5E7EB),
        Color(0xFFF9FAFB),
        Color(0xFFE5E7EB),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f),
    )
}

@Composable
private fun ShimmerGameCard(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(brush),
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
                .padding(horizontal = 4.dp),
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
                .padding(horizontal = 4.dp),
        )
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun ShimmerFilterChips() {
    val brush = shimmerBrush()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush),
            )
        }
    }
}