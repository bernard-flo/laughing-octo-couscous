package be.domain.player

import be.domain.common.SessionId
import be.domain.game.Game
import org.springframework.stereotype.Service
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult

@Service
class PlayerEnter(
    private val playerSessionRegistry: PlayerSessionRegistry,
    private val game: Game,
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

        return PlayerEnterResult.Success(
            currentGameStateInfo = game.getGameStateInfo(),
        )
    }

}
