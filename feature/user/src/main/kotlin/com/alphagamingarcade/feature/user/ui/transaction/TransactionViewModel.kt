package com.alphagamingarcade.feature.user.ui.transaction

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
) : ViewModel() {

    private val _transactionsUiState = MutableStateFlow(UiState(TransactionsScreenData()))
    val transactionsUiState = _transactionsUiState.asStateFlow()

    init {
        getTransactions()
    }

    fun getTransactions() {
        viewModelScope.launch {
            _transactionsUiState.value = _transactionsUiState.value.copy(
                loading = true,
                error = OneTimeEvent(null),
            )

            runCatching {
                // Replace this with your actual repository method.
                // Example:
                // rechargeRepository.getRechargeTransactions()

                mockTransactions()
            }.onSuccess { transactions ->
                _transactionsUiState.value = UiState(
                    data = TransactionsScreenData(
                        transactions = transactions,
                    ),
                    loading = false,
                )
            }.onFailure { throwable ->
                _transactionsUiState.value = _transactionsUiState.value.copy(
                    loading = false,
                    error = OneTimeEvent(throwable),
                )
            }
        }
    }

    private fun mockTransactions(): List<RechargeTransaction> {
        return listOf(
            RechargeTransaction(
                id = "1",
                amount = "₱500.00",
                method = "GCash",
                status = RechargeTransactionStatus.SUCCESS,
                referenceNumber = "RCG-20260408-0001",
                date = "Apr 08, 2026 • 10:15 AM",
            ),
            RechargeTransaction(
                id = "2",
                amount = "₱250.00",
                method = "PayMaya",
                status = RechargeTransactionStatus.PENDING,
                referenceNumber = "RCG-20260407-0002",
                date = "Apr 07, 2026 • 08:42 PM",
            ),
            RechargeTransaction(
                id = "3",
                amount = "₱1,000.00",
                method = "Credit Card",
                status = RechargeTransactionStatus.FAILED,
                referenceNumber = "RCG-20260406-0003",
                date = "Apr 06, 2026 • 01:03 PM",
            ),
            RechargeTransaction(
                id = "4",
                amount = "₱1,000.00",
                method = "Credit Card",
                status = RechargeTransactionStatus.FAILED,
                referenceNumber = "RCG-20260406-0003",
                date = "Apr 06, 2026 • 01:03 PM",
            ),
            RechargeTransaction(
                id = "5",
                amount = "₱1,000.00",
                method = "Credit Card",
                status = RechargeTransactionStatus.FAILED,
                referenceNumber = "RCG-20260406-0003",
                date = "Apr 06, 2026 • 01:03 PM",
            ),
            RechargeTransaction(
                id = "6",
                amount = "₱1,000.00",
                method = "Credit Card",
                status = RechargeTransactionStatus.FAILED,
                referenceNumber = "RCG-20260406-0003",
                date = "Apr 06, 2026 • 01:03 PM",
            ),

            RechargeTransaction(
                id = "7",
                amount = "₱1,000.00",
                method = "Credit Card",
                status = RechargeTransactionStatus.FAILED,
                referenceNumber = "RCG-20260406-0003",
                date = "Apr 06, 2026 • 01:03 PM",
            ),
        )
    }
}

/**
 * Data for [TransactionsScreen].
 *
 * @param transactions Recharge transaction list.
 */
@Immutable
data class TransactionsScreenData(
    val transactions: List<RechargeTransaction> = emptyList(),
)

/**
 * Recharge transaction model for UI.
 */
@Immutable
data class RechargeTransaction(
    val id: String,
    val amount: String,
    val method: String,
    val status: RechargeTransactionStatus,
    val referenceNumber: String,
    val date: String,
)

/**
 * Recharge transaction status.
 */
enum class RechargeTransactionStatus {
    SUCCESS,
    PENDING,
    FAILED,
}