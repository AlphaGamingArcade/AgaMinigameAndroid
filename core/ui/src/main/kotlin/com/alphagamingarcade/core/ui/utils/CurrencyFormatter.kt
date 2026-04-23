package com.alphagamingarcade.core.ui.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    /**
     * Format currency using locale (recommended for real apps)
     */
    fun format(
        amount: Double,
        currencyCode: String = "PHP",
        locale: Locale = Locale.getDefault()
    ): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.currency = Currency.getInstance(currencyCode)
        return formatter.format(amount)
    }

    /**
     * Force symbol (useful for games / consistent UI)
     */
    fun formatWithSymbol(
        amount: Double,
        symbol: String = "₱"
    ): String {
        return "$symbol${"%,.2f".format(amount)}"
    }

    /**
     * Compact format (1K, 1M, 1B) - good for games
     */
    fun formatCompact(amount: Double): String {
        return when {
            amount >= 1_000_000_000 -> "${"%.1f".format(amount / 1_000_000_000)}B"
            amount >= 1_000_000 -> "${"%.1f".format(amount / 1_000_000)}M"
            amount >= 1_000 -> "${"%.1f".format(amount / 1_000)}K"
            else -> "%.2f".format(amount)
        }
    }
}