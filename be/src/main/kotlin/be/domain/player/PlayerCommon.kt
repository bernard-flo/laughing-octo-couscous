package be.domain.player

import be.domain.SessionId


data class PlayerSession(
    val sessionId: SessionId,
    val playerName: PlayerName,
)

data class PlayerName(
    val value: String,
)
