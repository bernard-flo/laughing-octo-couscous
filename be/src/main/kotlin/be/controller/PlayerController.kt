package be.controller

import be.domain.common.SessionId
import be.domain.player.PlayerChat
import be.domain.player.PlayerEnter
import be.domain.player.PlayerGetQuizOutcome
import be.domain.player.PlayerRegisterAnswer
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.common.ChatUpdated
import shared.domain.player.PlayerChatCommandPayload
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerGetQuizOutcomeResult
import shared.domain.player.PlayerRegisterAnswerCommandPayload
import shared.domain.player.PlayerRegisterAnswerResult


@Controller
private class PlayerController(
    private val playerEnter: PlayerEnter,
    private val playerRegisterAnswer: PlayerRegisterAnswer,
    private val playerGetQuizOutcome: PlayerGetQuizOutcome,
    private val playerChat: PlayerChat,
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
    ): PlayerGetQuizOutcomeResult {

        return playerGetQuizOutcome(
            sessionId = SessionId(sessionId),
        )
    }

    @SendTo("/topic/chatUpdated")
    @MessageMapping("/player/chat")
    fun playerChatApi(
        @Header("simpSessionId") sessionId: String,
        playerChatCommandPayload: PlayerChatCommandPayload,
    ): ChatUpdated {

        return playerChat(
            sessionId = SessionId(sessionId),
            payload = playerChatCommandPayload,
        )
    }

}
