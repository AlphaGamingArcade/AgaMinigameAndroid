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
import com.alphagamingarcade.model.data.TransactionFreeDepositStatus
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

    private val _freeDepositStatus =
        MutableStateFlow(UiState(TransactionFreeDepositStatus(claimed = false, amount = 0.0)))
    val freeDepositStatus = _freeDepositStatus
        .onStart { checkRewardClaimed() }
        .stateInDelayed(
            UiState(TransactionFreeDepositStatus(claimed = false, amount = 0.0)),
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
            _freeDepositStatus.update { current ->
                current.copy(loading = true)
            }

            val resolvedMemberId = profileRepository
                .getProfileMember()
                .map { it.memberId }
                .first()

            runCatching {
                transactionRepository.getTransactionFreeDepositStatus(
                    memberId = resolvedMemberId
                )
            }.onSuccess { status ->
                _freeDepositStatus.update {
                    UiState(data = status)
                }
            }.onFailure { e ->
                _freeDepositStatus.update {
                    UiState(
                        data = TransactionFreeDepositStatus(claimed = false, amount = 0.0),
                        error = e.asOneTimeEvent()
                    )
                }
            }
        }
    }

    fun claimFreeDeposit() {
        if (_freeDepositStatus.value.loading) return
        viewModelScope.launch {
            _freeDepositStatus.update { current ->
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
                _freeDepositStatus.update {
                    UiState(data = status) // ✅ API returns updated status directly
                }
            }.onFailure { e ->
                _freeDepositStatus.update {
                    UiState(
                        data = TransactionFreeDepositStatus(claimed = false, amount = 0.0),
                        error = e.asOneTimeEvent()
                    )
                }
            }
        }
    }
}