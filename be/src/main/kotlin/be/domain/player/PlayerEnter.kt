package be.domain.player

import be.domain.SessionId
import org.springframework.stereotype.Service

@Service
class PlayerEnter(
    private val playerSessionRegistry: PlayerSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        playerName: PlayerName,
    ): PlayerEnterResult {

        val playerSession = PlayerSession(
            sessionId = sessionId,
            playerName = playerName,
        )
        playerSessionRegistry.add(playerSession)

        return PlayerEnterResult.Success
    }

}

sealed interface PlayerEnterResult {
    data object Success : PlayerEnterResult
    data object Fail : PlayerEnterResult
}
