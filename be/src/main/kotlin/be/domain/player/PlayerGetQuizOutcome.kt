package be.domain.player

import be.domain.common.SessionId
import be.domain.game.Game
import org.springframework.stereotype.Service
import shared.domain.game.PlayerQuizOutcome

@Service
class PlayerGetQuizOutcome(
    private val playerSessionRegistry: PlayerSessionRegistry,
    private val game: Game,
) {

    operator fun invoke(
        sessionId: SessionId,
    ): PlayerQuizOutcome {

        val playerSession = playerSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        return game.getPlayerQuizOutcome(playerSession.playerName)
    }

}
