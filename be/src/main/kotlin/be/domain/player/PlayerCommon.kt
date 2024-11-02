package be.domain.player

import be.domain.SessionId
import shared.domain.player.PlayerName


data class PlayerSession(
    val sessionId: SessionId,
    val playerName: PlayerName,
)
