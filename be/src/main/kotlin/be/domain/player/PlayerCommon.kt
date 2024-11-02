package be.domain.player

import be.domain.common.Session
import be.domain.common.SessionId
import shared.domain.player.PlayerName


data class PlayerSession(
    override val sessionId: SessionId,
    val playerName: PlayerName,
) : Session
