package com.alphagamingarcade.core.datastore.model

import kotlinx.serialization.Serializable

/**
 * Represents the agent data saved in Shared Preferences.
 *
 * @property id The unique identifier for the agent.
 * @property code The agent code.
 * @property currency The currency used by the agent.
 */
@Serializable
data class AgentPreferences(
    val id: Int = 0,
    val code: String = String(),
    val currency: String = String(),
)