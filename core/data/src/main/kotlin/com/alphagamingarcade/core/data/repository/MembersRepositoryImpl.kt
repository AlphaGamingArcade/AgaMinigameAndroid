package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.common.result.AppResult
import com.alphagamingarcade.core.common.utils.suspendRunAppResultCatching
import com.alphagamingarcade.core.datastore.data.UserPreferencesDataSource
import com.alphagamingarcade.core.network.data.MemberDataSource
import com.alphagamingarcade.core.network.model.asMemberPreferences
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.core.utils.suspendRunCatching
import com.alphagamingarcade.model.data.Game
import com.alphagamingarcade.model.data.Play
import kotlinx.coroutines.flow.first
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
    override suspend fun getMember(memberId: Int): Result<Unit> {
        return suspendRunCatching {
            val result = memberDataSource.getMember(memberId)
            userPreferencesDataSource.setUserMember(result.data.asMemberPreferences());
        }
    }

    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun createMember(account: String, nickname: String, dateOfBirth: String): AppResult<Unit> {
        return suspendRunAppResultCatching {
            val result = memberDataSource.createMember(account, nickname, dateOfBirth)
            userPreferencesDataSource.setUserMember(result.data.asMemberPreferences());
        }
    }

    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun updateMember(nickname: String): AppResult<Unit> {
        return suspendRunAppResultCatching {
            val memberId = userPreferencesDataSource.getMemberIdOrThrow()
            memberDataSource.updateMember(memberId, nickname)

            val currentPreferences = userPreferencesDataSource
                .getUserDataPreferences()
                .first()

            val memberPreferences = currentPreferences.member;
            if (memberPreferences != null){
                userPreferencesDataSource.setUserMember(
                    memberPreferences.copy(
                        nickname = nickname
                    )
                )
            }
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

    override suspend fun playGame(memberId: Int, gameId: Int): Result<Play> {
        return  suspendRunCatching {
            memberDataSource.playGame(
                memberId,
                gameId
            ).data.toExternalModel()
        }
    }
}