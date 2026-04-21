package com.alphagamingarcade.core.data.repository

import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun createMember(
        account: String,
        nickname: String,
        dateOfBirth: String,
    ): Result<Unit>

    suspend fun updateMember(
        nickname: String
    ): Result<Unit>
}