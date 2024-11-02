package be.controller

import be.domain.SessionId
import be.domain.presenter.PresenterEnter
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult


@Controller
private class PresenterController(
    private val presenterEnter: PresenterEnter,
) {

    @SendToUser("/topic/presenter/enter/result")
    @MessageMapping("/presenter/enter")
    fun presenterEnterApi(
        @Header("simpSessionId") sessionId: String,
        presenterEnterCommandPayload: PresenterEnterCommandPayload,
    ): PresenterEnterResult {

        return presenterEnter(
            sessionId = SessionId(sessionId),
            presenterEnterCommandPayload = presenterEnterCommandPayload,
        )
    }

}
