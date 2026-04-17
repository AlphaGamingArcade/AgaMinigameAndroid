package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.core.datastore.model.AgentPreferences
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkAgent(
    @SerialName("id")
    val id: Int,
    @SerialName("code")
    val code: String,
    @SerialName("currency")
    val currency: String,
)


fun NetworkAgent.asAgentPreferences() = AgentPreferences(
    id = id,
    code = code,
    currency = currency,
)