package com.alphagamingarcade.core.ui.utils

fun formatPlayerCount(count: Int): String = when {
    count >= 1_000_000 -> "%.1fM".format(count / 1_000_000f)
    count >= 1_000     -> "%.1fK".format(count / 1_000f)
    else               -> count.toString()
}

