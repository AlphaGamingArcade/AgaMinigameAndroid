package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.MemberDataSource
import com.alphagamingarcade.core.network.model.asMemberPreferences
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.core.utils.suspendRunCatching
import com.alphagamingarcade.model.data.Game
import javax.inject.Inject

class MembersRepositoryImpl @Inject constructor(
    private  val memberDataSource: MemberDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : MembersRepository {
    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun createMember(account: String, nickname: String, dateOfBirth: String): Result<Unit> {
        return suspendRunCatching {
            val result = memberDataSource.createMember(account, nickname, dateOfBirth)
            userPreferencesDataSource.setUserMember(result.data.asMemberPreferences());
        }
    }

    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun updateMember(nickname: String): Result<Unit> {
        return suspendRunCatching {
            val memberId = userPreferencesDataSource.getMemberIdOrThrow()
            memberDataSource.updateMember(memberId, nickname)
        }
    }

    override suspend fun getMemberFavorites(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Game>>{
        return  suspendRunCatching {
            memberDataSource.getMemberFavorites(
                memberId,
                pageNumber,
                pageSize,
            ).data.items.map { it.toExternalModel() }
        }
    }

    override suspend fun getMemberRecentPlayed(
        memberId: Int,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<Game>>{
        return  suspendRunCatching {
            memberDataSource.getMemberRecentPlayed(
                memberId,
                pageNumber,
                pageSize,
            ).data.items.map { it.toExternalModel() }
        }
    }

    override suspend fun removeFavorite(memberId: Int, gameId: Int): Result<Unit> {
        return  suspendRunCatching {
            memberDataSource.removeMemberFavorite(
                memberId,
                gameId
            )
        }
    }

    override suspend fun addFavorite(memberId: Int, gameId: Int): Result<Unit> {
        return  suspendRunCatching {
            memberDataSource.addMemberFavorite(
                memberId,
                gameId
            )
        }
    }
}