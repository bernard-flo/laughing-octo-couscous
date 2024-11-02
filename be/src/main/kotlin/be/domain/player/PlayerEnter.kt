package be.domain.player

import be.domain.common.SessionId
import org.springframework.stereotype.Service
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult

@Service
class PlayerEnter(
    private val playerSessionRegistry: PlayerSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        playerEnterCommandPayload: PlayerEnterCommandPayload,
    ): PlayerEnterResult {

        val playerSession = PlayerSession(
            sessionId = sessionId,
            playerName = playerEnterCommandPayload.playerName,
        )
        playerSessionRegistry.add(playerSession)

        return PlayerEnterResult.Success
    }

}
