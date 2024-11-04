package be.domain.player

import be.domain.common.SessionId
import be.domain.game.Game
import be.service.LoadPlayerService
import org.springframework.stereotype.Service
import shared.domain.player.InputPlayerName
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerName

@Service
class PlayerEnter(
    private val playerSessionRegistry: PlayerSessionRegistry,
    private val game: Game,
    loadPlayerService: LoadPlayerService,
) {

    private val playerNameList = loadPlayerService.playerInfoList.map { it.playerName }

    operator fun invoke(
        sessionId: SessionId,
        playerEnterCommandPayload: PlayerEnterCommandPayload,
    ): PlayerEnterResult {

        val inputPlayerName = playerEnterCommandPayload.playerName

        val playerName = findPlayerName(inputPlayerName)
        if (playerName == null) {
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

    private fun findPlayerName(inputPlayerName: InputPlayerName): PlayerName? {
        return playerNameList.find { it.value.lowercase() == inputPlayerName.value.lowercase() }
    }

}
