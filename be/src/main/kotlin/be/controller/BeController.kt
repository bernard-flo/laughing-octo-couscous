package be.controller

import be.domain.player.PlayerEnter
import be.domain.player.PlayerEnterResult
import be.domain.player.PlayerName
import be.domain.player.SessionId
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.message.PlayerEnterMessage
import shared.message.PlayerEnterResultMessage


@Controller
private class BeController(
    private val playerEnter: PlayerEnter,
) {

    @SendToUser("/topic/player/enter/result")
    @MessageMapping("/player/enter")
    fun playerEnterApi(
        @Header("simpSessionId") sessionId: String,
        playerEnterMessage: PlayerEnterMessage,
    ): PlayerEnterResultMessage {

        val result = playerEnter(
            sessionId = SessionId(sessionId),
            playerName = PlayerName(playerEnterMessage.playerName),
        )

        return PlayerEnterResultMessage(
            success = result is PlayerEnterResult.Success,
        )
    }

}
