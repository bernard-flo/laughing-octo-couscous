package be.domain.player

import be.domain.common.SessionId
import be.domain.game.Game
import be.domain.game.RegisterAnswerResult
import org.springframework.stereotype.Service
import shared.domain.player.PlayerRegisterAnswerCommandPayload
import shared.domain.player.PlayerRegisterAnswerResult

@Service
class PlayerRegisterAnswer(
    private val playerSessionRegistry: PlayerSessionRegistry,
    private val game: Game,
) {

    operator fun invoke(
        sessionId: SessionId,
        payload: PlayerRegisterAnswerCommandPayload,
    ): PlayerRegisterAnswerResult {

        val playerSession = playerSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        val ret = game.registerAnswer(playerSession.playerName, payload.answer)

        return when (ret) {
            RegisterAnswerResult.Success -> PlayerRegisterAnswerResult.Success(payload.answer)
            RegisterAnswerResult.Fail -> PlayerRegisterAnswerResult.Fail
        }
    }

}
