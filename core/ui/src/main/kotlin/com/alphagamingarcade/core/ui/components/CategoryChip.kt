package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Palette ─────────────────────────────────────────────────────────────────
private val SurfaceGray    = Color(0xFFF5F6FA)
// ─── Category Chips ──────────────────────────────────────────────────────────

@Composable
fun FilterChips(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    LazyRow(
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
                    labelColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = null,
            )
        }
    }
}