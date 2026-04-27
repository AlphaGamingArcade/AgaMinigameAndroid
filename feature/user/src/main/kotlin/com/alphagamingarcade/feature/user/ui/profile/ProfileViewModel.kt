package com.alphagamingarcade.feature.user.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphagamingarcade.core.data.model.Profile
import com.alphagamingarcade.core.data.repository.ProfileRepository
import com.alphagamingarcade.core.data.repository.TransactionRepository
import com.alphagamingarcade.core.extensions.asOneTimeEvent
import com.alphagamingarcade.core.extensions.stateInDelayed
import com.alphagamingarcade.core.ui.utils.UiState
import com.alphagamingarcade.core.ui.utils.updateWith
import com.alphagamingarcade.model.data.TransactionFreeDeposit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(UiState(Profile()))
    val profileUiState = _profileUiState
        .onStart { updateProfileData() }
        .stateInDelayed(UiState(Profile()), viewModelScope)

    private val _freeDeposit =
        MutableStateFlow(UiState(TransactionFreeDeposit(claimed = false, amount = 0.0, currency = "USD")))
    val freeDeposit = _freeDeposit
        .onStart { checkRewardClaimed() }
        .stateInDelayed(
            UiState(TransactionFreeDeposit(claimed = false, amount = 0.0, currency = "USD")),
            viewModelScope
        )

    private fun updateProfileData() {
        profileRepository.getProfile()
            .map { profile -> UiState(profile) }
            .onEach { data -> _profileUiState.update { data } }
            .catch { e ->
                _profileUiState.update {
                    UiState(Profile(), error = e.asOneTimeEvent())
                }
            }
            .launchIn(viewModelScope)
    }

    fun signOut() {
        _profileUiState.updateWith { profileRepository.signOut() }
    }

    private fun checkRewardClaimed() {
        viewModelScope.launch {
            _freeDeposit.update { current ->
                current.copy(loading = true)
            }

            val resolvedMemberId = profileRepository
                .getProfileMember()
                .map { it.memberId }
                .first()

            runCatching {
                transactionRepository.getTransactionFreeDeposit(
                    memberId = resolvedMemberId
                )
            }.onSuccess { status ->
                _freeDeposit.update {
                    UiState(data = status)
                }
            }.onFailure { e ->
                _freeDeposit.update {
                    UiState(
                        data = TransactionFreeDeposit(claimed = false, amount = 0.0, currency = "USD"),
                        error = e.asOneTimeEvent()
                    )
                }
            }
        }
    }

    fun claimFreeDeposit() {
        if (_freeDeposit.value.loading) return
        viewModelScope.launch {
            _freeDeposit.update { current ->
                current.copy(loading = true)
            }

            val resolvedMemberId = profileRepository
                .getProfileMember()
                .map { it.memberId }
                .first()

            runCatching {
                transactionRepository.claimTransactionFreeDeposit(
                    memberId = resolvedMemberId
                )
            }.onSuccess { status ->
                _freeDeposit.update {
                    UiState(data = status)
                }

                val currentBalance = profileRepository
                    .getProfileMember()
                    .map { it.gameMoney }
                    .first()

                profileRepository.updateUserBalance(currentBalance + status.amount)

            }.onFailure { e ->
                _freeDeposit.update {
                    UiState(
                        data = TransactionFreeDeposit(claimed = false, amount = 0.0, currency = "USD"),
                        error = e.asOneTimeEvent()
                    )
                }
            }
        }
    }
}