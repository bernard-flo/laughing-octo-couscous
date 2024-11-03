package be.domain.manager

import be.domain.common.SessionId
import be.domain.game.Game
import org.springframework.stereotype.Service
import shared.domain.common.GameStateUpdated

@Service
class ManagerToAnsweredState(
    private val managerSessionRegistry: ManagerSessionRegistry,
    private val game: Game,
) {

    operator fun invoke(
        sessionId: SessionId,
    ): GameStateUpdated {

        managerSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        game.toAnsweredState()

        return GameStateUpdated(
            gameStateInfo = game.getGameStateInfo(),
        )
    }

}
