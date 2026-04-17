package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.MemberDataSource
import com.alphagamingarcade.core.network.model.asMemberPreferences
import com.alphagamingarcade.core.utils.suspendRunCatching
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private  val memberDataSource: MemberDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : MemberRepository {
    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun createMember(account: String, nickname: String, dateOfBirth: String): Result<Unit> {
        return suspendRunCatching {
            val result = memberDataSource.createMember(account, nickname, dateOfBirth)
                .getOrThrow()
            userPreferencesDataSource.setUserMember(result.data.asMemberPreferences());
        }
    }
}