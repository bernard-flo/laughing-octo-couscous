package be.domain.manager

import be.domain.common.SessionId
import be.domain.game.Game
import org.springframework.stereotype.Service
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult

@Service
class ManagerEnter(
    private val managerSessionRegistry: ManagerSessionRegistry,
    private val game: Game,
) {

    operator fun invoke(
        sessionId: SessionId,
        managerEnterCommandPayload: ManagerEnterCommandPayload,
    ): ManagerEnterResult {

        val managerSession = ManagerSession(
            sessionId = sessionId,
        )
        managerSessionRegistry.add(managerSession)

        return ManagerEnterResult.Success(
            currentGameStateInfo = game.getGameStateInfo(),
        )
    }

}
