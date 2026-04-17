package com.alphagamingarcade.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberPreferences(
    val id: Int = 0,
    val agentId: Int = 0,
    val account: String = String(),
    val nickname: String = String(),
    val gameMoney: Double = 0.00,
    val nicknameUpdate: String? = null,
    val userId: Int = 0,
    val agent: AgentPreferences = AgentPreferences(),
)
