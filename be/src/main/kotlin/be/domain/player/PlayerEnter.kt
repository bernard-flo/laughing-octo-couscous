package be.domain.player

import be.domain.common.SessionId
import be.domain.game.Game
import be.service.ResourceService
import org.springframework.stereotype.Service
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult

@Service
class PlayerEnter(
    private val playerSessionRegistry: PlayerSessionRegistry,
    private val game: Game,
    resourceService: ResourceService,
) {

    private val availablePlayerNames = resourceService.loadPlayerNames()

    operator fun invoke(
        sessionId: SessionId,
        playerEnterCommandPayload: PlayerEnterCommandPayload,
    ): PlayerEnterResult {

        val playerName = playerEnterCommandPayload.playerName

        if (availablePlayerNames.contains(playerName) == false) {
            return PlayerEnterResult.Fail
        }

        val playerSession = PlayerSession(
            sessionId = sessionId,
            playerName = playerName,
        )
        playerSessionRegistry.add(playerSession)

        return PlayerEnterResult.Success(
            currentGameStateInfo = game.getGameStateInfo(),
        )
    }

}
