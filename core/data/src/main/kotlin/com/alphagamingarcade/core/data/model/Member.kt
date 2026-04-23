package com.alphagamingarcade.core.data.model

import com.alphagamingarcade.core.datastore.model.UserDataPreferences
import kotlin.Int


data class Member(
    val memberId: Int,
    val agentId: Int,
    val account: String,
    val nickname: String,
    val gameMoney: Double,
)


/**
 * Extension function to convert UserDataPreferences to Profile.
 *
 * @return A Profile object with data from UserDataPreferences.
 */
fun UserDataPreferences.toMember(): Member {
    return Member(
        memberId = member?.id ?: 0,
        agentId = member?.agentId ?: 0,
        account = member?.account ?: "",
        nickname = member?.nickname ?: "",
        gameMoney = member?.gameMoney ?: 0.0
    )
}
