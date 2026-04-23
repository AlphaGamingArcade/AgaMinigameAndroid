package com.alphagamingarcade.feature.games.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable
import com.alphagamingarcade.model.data.Banner
import com.alphagamingarcade.model.data.Game

// ─── Palette ─────────────────────────────────────────────────────────────────

private val AccentOrange = Color(0xFFFF6B35)
private val AccentPurple = Color(0xFF7B2FBE)
private val AccentGold   = Color(0xFFFFBF00)
private val TagNew       = Color(0xFF00C48C)
private val TagHot       = Color(0xFFFF4757)
private val SurfaceGray  = Color(0xFFF5F6FA)
private val TextPrimary  = Color(0xFF1A1A2E)
private val TextSecondary= Color(0xFF8A8A9A)

// ─── Entry Point ─────────────────────────────────────────────────────────────

@Composable
internal fun GamesScreen(
    onGameClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    gamesViewModel: GamesViewModel = hiltViewModel(),
) {
    val gamesState by gamesViewModel.gamesUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = gamesState,
        onShowSnackbar = onShowSnackbar,
    ) { data ->
        GamesScreen(
            data = data,
            onGameClick = onGameClick,
            onCategoryClick = onCategoryClick
        )
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun GamesScreen(
    data: GamesScreenData,
    onGameClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {

            // ── Hero Banner ──────────────────────────────────────────────────
            item {
                HeroBannerCarousel(
                    banners = data.bannerGames,
                    onGameClick = onGameClick,
                )
                Spacer(Modifier.height(24.dp))
            }

            // ── Quick Category Pills ─────────────────────────────────────────
            item {
                SectionTitle(
                    title = "Categories",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                QuickCategoryPills(onCategoryClick = onCategoryClick)
                Spacer(Modifier.height(28.dp))
            }

            // ── Jackpot Banner ───────────────────────────────────────────────
//            item {
//                JackpotBanner(
//                    games = data.jackpotGames,
//                    onGameClick = onGameClick,
//                )
//                Spacer(Modifier.height(28.dp))
//            }

            // ── Trending Now ─────────────────────────────────────────────────
            item {
                SectionHeader(
                    title = "Trending Now",
                    subtitle = "Most played today",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                TrendingRow(games = data.trendingGames, onGameClick = onGameClick)
                Spacer(Modifier.height(28.dp))
            }

            // ── New Releases ─────────────────────────────────────────────────
            item {
                SectionHeader(
                    title = "New Releases",
                    subtitle = "Fresh off the press",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                NewReleasesRow(games = data.newReleases, onGameClick = onGameClick)
                Spacer(Modifier.height(28.dp))
            }

            // ── Coming Soon ──────────────────────────────────────────────────
            item {
                SectionHeader(
                    title = "Coming Soon",
                    subtitle = "Stay tuned for what's next",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                ComingSoonRow(games = data.comingSoonGames)
                Spacer(Modifier.height(28.dp))
            }


            // ── Top Rated ────────────────────────────────────────────────────
            item {
                SectionHeader(
                    title = "Top Rated",
                    subtitle = "Players' all-time favourites",
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
            }

            items(items = data.topRated, key = { it.id }) { game ->
                TopRatedListItem(
                    game = game,
                    rank = data.topRated.indexOf(game) + 1,
                    onClick = { onGameClick(game.id.toString()) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                )
            }
        }
    }
}

// ─── Hero Banner Carousel ────────────────────────────────────────────────────

@Composable
private fun HeroBannerCarousel(
    banners: List<Banner>,
    onGameClick: (String) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 12.dp,
        ) { page ->
            val banner = banners[page]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onGameClick(banner.id.toString()) },
            ) {
                AsyncImage(
                    model = banner.imageUrl,
                    contentDescription = banner.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                            ),
                        ),
                )
                // Labels
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                ) {
                    if (banner.isNew) {
                        GameTag(label = "NEW", color = TagNew)
                        Spacer(Modifier.height(6.dp))
                    }
                    Text(
                        text = banner.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "Tap to play",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                    )
                }
            }
        }

        // Pager dots
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(banners.size) { i ->
                val isSelected = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 20.dp else 6.dp, 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) AccentOrange else SurfaceGray),
                )
            }
        }
    }
}

// ─── Quick Category Pills ────────────────────────────────────────────────────

private val quickCategories = listOf(
    "🎰" to "Slots",
    "🃏" to "Table",
    "🎲" to "Live",
    "⚽" to "Sports",
    "🎮" to "Arcade",
    "💎" to "VIP",
)

@Composable
private fun QuickCategoryPills(onCategoryClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(quickCategories) { (icon, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onCategoryClick(label) }
                    .background(SurfaceGray)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(text = icon, fontSize = 24.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
            }
        }
    }
}

// ─── Coming Soon Row ──────────────────────────────────────────────────────────

@Composable
private fun ComingSoonRow(games: List<Game>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            ComingSoonCard(game = game)
        }
    }
}

@Composable
private fun ComingSoonCard(game: Game) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceGray, RoundedCornerShape(16.dp)),
    ) {
        Column {
            // Image with locked overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
            ) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                // Dark overlay — makes it look locked/unavailable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f)),
                )
                // Lock icon centered
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentPurple.copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text = "COMING SOON",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                        )
                    }
                }
            }

            // Game info
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = game.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ─── Jackpot Banner ──────────────────────────────────────────────────────────

@Composable
private fun JackpotBanner(
    games: List<Game>,
    onGameClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(AccentPurple, Color(0xFF3A1078)),
                ),
            )
            .padding(20.dp),
    ) {
        Column {
            Text(
                text = "💰 JACKPOT GAMES",
                color = AccentGold,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 1.5.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Win up to ₱1,000,000",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(games) { game ->
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
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
                                .background(Color.Black.copy(alpha = 0.3f)),
                        )
                        Text(
                            text = game.name,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(6.dp),
                        )
                    }
                }
            }
        }
    }
}

// ─── Trending Row ─────────────────────────────────────────────────────────────

@Composable
private fun TrendingRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            TrendingCard(game = game, onClick = { onGameClick(game.id.toString()) })
        }
    }
}

@Composable
private fun TrendingCard(game: Game, onClick: () -> Unit) {
    Card(
        onClick = onClick,             // 👈 use Card's built-in onClick
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            ) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                if (game.isTrending) {
                    GameTag(
                        label = "HOT",
                        color = TagHot,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp),
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = game.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(text = "⭐ 4.8", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

// ─── New Releases Row ─────────────────────────────────────────────────────────

@Composable
private fun NewReleasesRow(games: List<Game>, onGameClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            NewReleaseCard(game = game, onClick = { onGameClick(game.id.toString()) })
        }
    }
}

@Composable
private fun NewReleaseCard(game: Game, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clip(RoundedCornerShape(16.dp))  // 👈 clip BEFORE clickable
            .clickable(onClick = onClick),
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
                label = "NEW",
                color = TagNew,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
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

// ─── Top Rated List Item ──────────────────────────────────────────────────────

@Composable
private fun TopRatedListItem(
    game: Game,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, SurfaceGray, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> AccentGold
                            2 -> Color(0xFFB0BEC5)
                            3 -> Color(0xFFCD7F32)
                            else -> SurfaceGray
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "#$rank",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = if (rank <= 3) Color.White else TextSecondary,
                )
            }

            Spacer(Modifier.width(12.dp))

            // Game thumbnail
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )

            Spacer(Modifier.width(12.dp))

            // Game info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary,
                )
                Spacer(Modifier.height(4.dp))
                Text(text = "⭐ 4.9 · 120K plays", fontSize = 12.sp, color = TextSecondary)
            }

            // Hot tag if applicable
            if (game.isTrending) {
                GameTag(label = "HOT", color = TagHot)
            }
        }
    }
}

// ─── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = TextPrimary,
        modifier = modifier,
    )
}

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