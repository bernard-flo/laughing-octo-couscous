package be.controller

import be.domain.SessionId
import be.domain.player.PlayerEnter
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult


@Controller
private class PlayerController(
    private val playerEnter: PlayerEnter,
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

}
