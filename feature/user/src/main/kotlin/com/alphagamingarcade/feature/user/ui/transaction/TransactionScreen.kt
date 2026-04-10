package com.alphagamingarcade.feature.user.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.core.ui.utils.StatefulComposable

private val ScreenBackground = Color.White
private val CardBackground = Color.White
private val BorderGray = Color(0xFFE9EDF2)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF7A8194)

/**
 * Transactions screen.
 *
 * @param onBackClick Navigate back.
 * @param onShowSnackbar Show Snackbar.
 * @param viewModel [TransactionsViewModel].
 */
@Composable
internal fun TransactionScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    viewModel: TransactionsViewModel = hiltViewModel(),
) {
    val transactionsState by viewModel.transactionsUiState.collectAsStateWithLifecycle()

    StatefulComposable(
        state = transactionsState,
        onShowSnackbar = onShowSnackbar,
    ) { screenData ->
        TransactionsScreen(
            screenData = screenData,
            onBackClick = onBackClick,
        )
    }
}

/**
 * Transactions screen content.
 *
 * @param screenData [TransactionsScreenData].
 * @param onBackClick Navigate back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsScreen(
    screenData: TransactionsScreenData,
    onBackClick: () -> Unit,
) {
    Surface(
        color = ScreenBackground,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
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
                        text = "Transactions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = TextPrimary,
                    titleContentColor = TextPrimary,
                ),
            )

            if (screenData.transactions.isEmpty()) {
                EmptyTransactionsContent()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Text(
                            text = "Your recharge transactions",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Review your recent top-ups and payment status.",
                            fontSize = 14.sp,
                            color = TextSecondary,
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(
                        items = screenData.transactions,
                        key = { it.id },
                    ) { transaction ->
                        TransactionItem(transaction = transaction)
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionsContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = "No Transactions",
                tint = TextPrimary,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No recharge transactions yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your completed or pending recharge records will appear here.",
                fontSize = 14.sp,
                color = TextSecondary,
            )
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: RechargeTransaction,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(14.dp),
            )
            .background(CardBackground)
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = transaction.amount,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )

                StatusChip(status = transaction.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = transaction.method,
                fontSize = 13.sp,
                color = TextSecondary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = BorderGray.copy(alpha = 0.4f),
                thickness = 0.5.dp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            TransactionInfoRow(
                label = "Reference No.",
                value = transaction.referenceNumber,
            )

            TransactionInfoRow(
                label = "Date",
                value = transaction.date,
            )
        }
    }
}

@Composable
private fun StatusChip(status: RechargeTransactionStatus) {
    val (icon, text, containerColor, contentColor) = when (status) {
        RechargeTransactionStatus.SUCCESS -> Quadruple(
            Icons.Default.CheckCircle,
            "Success",
            Color(0xFFE8F8F1),
            Color(0xFF059669),
        )

        RechargeTransactionStatus.PENDING -> Quadruple(
            Icons.Default.Schedule,
            "Pending",
            Color(0xFFFFF7E0),
            Color(0xFFD97706),
        )

        RechargeTransactionStatus.FAILED -> Quadruple(
            Icons.Default.Warning,
            "Failed",
            Color(0xFFFFEFEF),
            Color(0xFFE53935),
        )
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(12.dp),
        )

        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor,
        )
    }
}

@Composable
private fun TransactionInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary.copy(alpha = 0.8f),
        )

        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
        )
    }
}

/**
 * Small helper because Kotlin has Pair and Triple only.
 */
private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
)