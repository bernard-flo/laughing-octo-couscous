package be.controller

import be.domain.common.SessionId
import be.domain.player.PlayerEnter
import be.domain.player.PlayerGetQuizOutcome
import be.domain.player.PlayerRegisterAnswer
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.game.PlayerQuizOutcome
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerRegisterAnswerCommandPayload
import shared.domain.player.PlayerRegisterAnswerResult


@Controller
private class PlayerController(
    private val playerEnter: PlayerEnter,
    private val playerRegisterAnswer: PlayerRegisterAnswer,
    private val playerGetQuizOutcome: PlayerGetQuizOutcome,
) {

    @SendToUser("/topic/player/enter/result")
    @MessageMapping("/player/enter")
    fun playerEnterApi(
        @Header("simpSessionId") sessionId: String,
        playerEnterCommandPayload: PlayerEnterCommandPayload,
    ): PlayerEnterResult {

        return playerEnter(
            sessionId = SessionId(sessionId),
            playerEnterCommandPayload = playerEnterCommandPayload,
        )
    }

    @SendToUser("/topic/player/registerAnswer/result")
    @MessageMapping("/player/registerAnswer")
    fun playerRegisterAnswerApi(
        @Header("simpSessionId") sessionId: String,
        playerRegisterAnswerCommandPayload: PlayerRegisterAnswerCommandPayload,
    ): PlayerRegisterAnswerResult {

        return playerRegisterAnswer(
            sessionId = SessionId(sessionId),
            payload = playerRegisterAnswerCommandPayload,
        )
    }

    @SendToUser("/topic/player/getQuizOutcome/result")
    @MessageMapping("/player/getQuizOutcome")
    fun getQuizOutcomeApi(
        @Header("simpSessionId") sessionId: String,
    ): PlayerQuizOutcome {

        return playerGetQuizOutcome(
            sessionId = SessionId(sessionId),
        )
    }

}
