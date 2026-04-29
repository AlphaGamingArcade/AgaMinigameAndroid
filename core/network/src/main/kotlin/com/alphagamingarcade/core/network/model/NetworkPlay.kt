package com.alphagamingarcade.core.network.model

import com.alphagamingarcade.model.data.Play
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlay(
    @SerialName("id")
    val playId : Int,
    val memberId: Int,
    val gameId: Int,
    val playUrl: String,
)


@Serializable
data class NetworkPlayRequest(
    val gameId: Int
)

fun NetworkPlay.toExternalModel() = Play(
    playId = playId,
    memberId = memberId,
    playUrl = playUrl,
)