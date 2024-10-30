package be.controller

import be.domain.SessionId
import be.domain.presenter.PresenterEnter
import be.domain.presenter.PresenterEnterResult
import be.domain.presenter.PresenterPassword
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.message.PresenterEnterMessage
import shared.message.PresenterEnterResultMessage


@Controller
private class PresenterController(
    private val presenterEnter: PresenterEnter,
) {

    @SendToUser("/topic/presenter/enter/result")
    @MessageMapping("/presenter/enter")
    fun presenterEnterApi(
        @Header("simpSessionId") sessionId: String,
        presenterEnterMessage: PresenterEnterMessage,
    ): PresenterEnterResultMessage {

        val result = presenterEnter(
            sessionId = SessionId(sessionId),
            presenterPassword = PresenterPassword(presenterEnterMessage.password),
        )

        println("presenterEnterApi: $result")

        return PresenterEnterResultMessage(
            success = result is PresenterEnterResult.Success,
        )
    }

}
