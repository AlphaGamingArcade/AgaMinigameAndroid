package com.alphagamingarcade.feature.user.ui.transaction

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.utils.OneTimeEvent
import com.alphagamingarcade.model.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var memberId: Int? = null

    private val _transactionsUiState = MutableStateFlow(
        UiState(
            data = TransactionsScreenData(),
            loading = true
        )
    )
    val transactionsUiState = _transactionsUiState.asStateFlow()

    init {
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScope.launch {
            try {
                val resolvedMemberId = profileRepository
                    .getProfileMember()
                    .map { it.memberId }
                    .first()

                memberId = resolvedMemberId

                val pageSize = _transactionsUiState.value.data.pageSize

                _transactionsUiState.value = _transactionsUiState.value.copy(
                    data = _transactionsUiState.value.data.copy(
                        pageNumber = 1,
                        endReached = false,
                        transactions = emptyList()
                    )
                )

                val transactions = transactionRepository
                    .getTransactions(resolvedMemberId, 1, pageSize)

                _transactionsUiState.value = UiState(
                    data = _transactionsUiState.value.data.copy(
                        transactions = transactions,
                        pageNumber = 2,
                        endReached = transactions.size < pageSize
                    ),
                    loading = false
                )

            } catch (e: Exception) {
                _transactionsUiState.value = UiState(
                    data = _transactionsUiState.value.data.copy(),
                    loading = false,
                    error = OneTimeEvent(e)
                )
            }
        }
    }

    fun loadMore() {
        val state = _transactionsUiState.value.data
        val currentMemberId = memberId ?: return

        if (state.isLoadingMore || state.endReached) return

        viewModelScope.launch {
            try {
                _transactionsUiState.value = _transactionsUiState.value.copy(
                    data = state.copy(isLoadingMore = true)
                )

                val nextPage = state.pageNumber
                val pageSize = state.pageSize

                val newTransactions = transactionRepository
                    .getTransactions(currentMemberId, nextPage, pageSize) // ✅ no .first()

                val updatedList = state.transactions + newTransactions

                _transactionsUiState.value = UiState(
                    data = state.copy(
                        transactions = updatedList,
                        pageNumber = nextPage + 1,
                        isLoadingMore = false,
                        endReached = newTransactions.size < pageSize
                    ),
                    loading = false
                )

            } catch (e: Exception) {
                _transactionsUiState.value = UiState(
                    data = state.copy(isLoadingMore = false),
                    loading = false,
                    error = OneTimeEvent(e)
                )
            }
        }
    }
}

@Immutable
data class TransactionsScreenData(
    val transactions: List<Transaction> = emptyList(),
    val pageNumber: Int = 1,
    val pageSize: Int = 5,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
)