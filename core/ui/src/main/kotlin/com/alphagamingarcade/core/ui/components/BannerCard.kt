package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.alphagamingarcade.core.ui.utils.PreviewDevices
import com.alphagamingarcade.core.ui.utils.PreviewThemes
import com.alphagamingarcade.model.data.Banner

@Composable
fun BannerCard(banner: Banner) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image layer
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = banner.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay on top of image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }
    }
}

@PreviewThemes
@PreviewDevices
@Composable
private fun BannerCardPreview(){
    BannerCard(
        banner = Banner(
            id = 1,
            name = "Banner",
            imageUrl = "Image url",
            description = "Description"
        )
    )
}