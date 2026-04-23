package com.alphagamingarcade.feature.gamedetail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable

private val ScreenBackground = Color.White
private val CardBackground = Color.White
private val BorderGray = Color(0xFFE9EDF2)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF7A8194)
private val AccentColor = Color(0xFF0F8CA0)
private val AccentSoft = Color(0xFFE8F7FA)

/**
 * Game detail screen.
 *
 * @param onBackClick Navigate back.
 * @param onShowSnackbar Show Snackbar.
 * @param onPlayClick Callback when play button is clicked.
 * @param viewModel [GameDetailViewModel].
 */
@Composable
internal fun GameDetailScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    onPlayClick: (String) -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel(),
) {
    val gameDetailState by viewModel.gameDetailUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = gameDetailState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        GameDetailScreen(
            screenData = screenData,
            onBackClick = onBackClick,
            onFavoriteClick = viewModel::toggleFavorite,
            onScreenshotClick = viewModel::selectScreenshot,
            onPlayClick = { onPlayClick(screenData.game.id) },
            onSimilarGameClick = viewModel::openSimilarGame,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailScreen(
    screenData: GameDetailScreenData,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onScreenshotClick: (String) -> Unit,
    onPlayClick: () -> Unit,
    onSimilarGameClick: (String) -> Unit,
) {
    val listState = rememberLazyListState()

    val showCollapsedTopBar by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 8
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                GameHeroSection(
                    game = screenData.game,
                    onBackClick = onBackClick,
                    onFavoriteClick = onFavoriteClick,
                    onPlayClick = onPlayClick,
                    showBackButton = !showCollapsedTopBar,
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    ScreenshotGallery(
                        screenshots = screenData.game.screenshotUrls,
                        selectedScreenshotUrl = screenData.selectedScreenshotUrl,
                        onScreenshotClick = onScreenshotClick,
                    )

                    SectionBlock(
                        title = "About this game",
                        content = screenData.game.description,
                    )

                    SimilarGamesSection(
                        games = screenData.similarGames,
                        onGameClick = onSimilarGameClick,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showCollapsedTopBar,
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary,
                            )
                        }
                    },
                    title = {
                        Text(
                            text = screenData.game.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        navigationIconContentColor = TextPrimary,
                        titleContentColor = TextPrimary,
                    ),
                )
            }
        }

    }
}

@Composable
private fun GameHeroSection(
    game: GameDetailUiModel,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onPlayClick: () -> Unit,
    showBackButton: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(AccentSoft),
            ) {
                AsyncImage(
                    model = game.bannerUrl,
                    contentDescription = "Game Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = showBackButton,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    AsyncImage(
                        model = game.iconUrl,
                        contentDescription = game.title,
                        modifier = Modifier
                            .size(72.dp)
                            .offset(y = (-36).dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentSoft)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp),
                            ),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 2.dp),
                    ) {
                        Text(
                            text = game.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = game.category,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = AccentColor,
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = game.playerCountLabel,
                            fontSize = 13.sp,
                            color = TextSecondary,
                        )
                    }

                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.padding(top = 2.dp),
                    ) {
                        Icon(
                            imageVector = if (game.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (game.isFavorite) AccentColor else TextSecondary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor,
                        contentColor = Color.White,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        disabledElevation = 0.dp,
                    ),
                ) {
                    Text(
                        text = "Play",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenshotGallery(
    screenshots: List<String>,
    selectedScreenshotUrl: String,
    onScreenshotClick: (String) -> Unit,
) {
    if (screenshots.isEmpty()) return

    val initialPage = screenshots.indexOf(selectedScreenshotUrl).takeIf { it >= 0 } ?: 0
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { screenshots.size },
    )

    LaunchedEffect(pagerState.currentPage) {
        val current = screenshots.getOrNull(pagerState.currentPage) ?: return@LaunchedEffect
        if (current != selectedScreenshotUrl) {
            onScreenshotClick(current)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // ── Title ─────────────────────────────
        Text(
            text = "Previews",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        // ── Pager ─────────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(end = 72.dp),
            pageSpacing = 12.dp,
        ) { page ->
            val screenshot = screenshots[page]
            val isSelected = page == pagerState.currentPage

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isSelected) 172.dp else 168.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) BorderGray else BorderGray.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .background(Color.White)
                    .clickable { onScreenshotClick(screenshot) },
            ) {
                AsyncImage(
                    model = screenshot,
                    contentDescription = "Screenshot ${page + 1}",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun SectionBlock(
    title: String,
    content: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(18.dp),
            )
            .padding(18.dp),
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            thickness = 0.6.dp,
            color = BorderGray.copy(alpha = 0.5f),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = content,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = TextSecondary.copy(alpha = 0.95f),
        )
    }
}

@Composable
private fun SimilarGamesSection(
    games: List<SimilarGameUiModel>,
    onGameClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Similar games",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(games) { game ->
                SimilarGameItem(
                    game = game,
                    onClick = { onGameClick(game.id) },
                )
            }
        }
    }
}

@Composable
private fun SimilarGameItem(
    game: SimilarGameUiModel,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(18.dp),
            )
            .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        AsyncImage(
            model = game.thumbnailUrl,
            contentDescription = game.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(14.dp))
                .background(AccentSoft),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = game.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = game.category,
            fontSize = 12.sp,
            color = TextSecondary,
            maxLines = 1,
        )
    }
}