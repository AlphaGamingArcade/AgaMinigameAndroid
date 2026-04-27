package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.core.datastore.model.MemberPreferences
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCreateMemberRequest(
    @SerialName("account")
    val account: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("dateOfBirth")
    val dateOfBirth: String,
)

@Serializable
data class  NetworkUpdateMemberRequest(
    @SerialName("nickname")
    val nickname: String,
)


@Serializable
data object NetworkUpdateMemberResponse

@Serializable
data class NetworkMember(
    @SerialName("id")
    val id: Int,
    @SerialName("agentId")
    val agentId: Int,
    @SerialName("account")
    val account: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("gamemoney")
    val gameMoney: Double,
    @SerialName("nicknameUpdate")
    val nicknameUpdate: String? = null,
    @SerialName("userId")
    val userId: Int,
    @SerialName("agent")
    val agent: NetworkAgent,
    @SerialName("user")
    val user: NetworkUser,
)

fun NetworkMember.asMemberPreferences() = MemberPreferences(
    id = id,
    agentId = agentId,
    account = account,
    nickname = nickname,
    gameMoney = gameMoney,
    nicknameUpdate = nicknameUpdate,
    userId = userId,
    agent = agent.asAgentPreferences(),
)