package be.domain.player

import be.domain.SessionId
import org.springframework.stereotype.Service

@Service
class PlayerSessionRegistry {

    private val playerSessionMap: MutableMap<SessionId, PlayerSession> = mutableMapOf()

    fun add(playerSession: PlayerSession) {

        playerSessionMap[playerSession.sessionId] = playerSession
    }

    fun get(sessionId: SessionId): PlayerSession? {

        return playerSessionMap[sessionId]
    }

}
