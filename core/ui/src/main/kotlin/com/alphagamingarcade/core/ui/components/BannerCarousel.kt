package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alphagamingarcade.model.data.Banner
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel(banners: List<Banner>) {
    // Create a very large page count for "infinite" scrolling
    val infinitePageCount = Int.MAX_VALUE
    val actualPageCount = banners.size
    val initialPage = infinitePageCount / 2 // Start in the middle

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { infinitePageCount }
    )

    // Auto-scroll effect (optional)
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000) // Wait 3 seconds
            val nextPage = pagerState.currentPage + 1
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        ) { page ->
            val actualPage = page % actualPageCount
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BannerCard(banner = banners[actualPage])
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                // Show indicator based on actual page position
                val isSelected = (pagerState.currentPage % actualPageCount) == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.onBackground
                            else
                                MaterialTheme.colorScheme.onSecondary
                        )
                )
            }
        }
    }
}