package be.controller

import be.domain.common.SessionId
import be.domain.manager.ManagerEnter
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult


@Controller
private class ManagerController(
    private val managerEnter: ManagerEnter,
) {

    @SendToUser("/topic/manager/enter/result")
    @MessageMapping("/manager/enter")
    fun managerEnterApi(
        @Header("simpSessionId") sessionId: String,
        managerEnterCommandPayload: ManagerEnterCommandPayload,
    ): ManagerEnterResult {

        return managerEnter(
            sessionId = SessionId(sessionId),
            managerEnterCommandPayload = managerEnterCommandPayload,
        )
    }

}
