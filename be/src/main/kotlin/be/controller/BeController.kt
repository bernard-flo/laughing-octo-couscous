package be.controller

import be.configuration.WebSocketClosedEvent
import be.configuration.WebSocketEstablishedEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.stereotype.Controller
import shared.Greeting
import shared.HelloMessage


@Controller
private class BeController(
    private val messagingTemplate: SimpMessageSendingOperations,
) {

    @SendTo("/topic/reply")
    @MessageMapping("/hello")
    fun greeting(
        message: HelloMessage,
        @Header("simpSessionId") sessionId: String,
    ): Greeting {

        messagingTemplate.convertAndSendToUser(
            sessionId,
            "/topic/reply",
            Greeting("Hello"),
            SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE).let {
                it.sessionId = sessionId
                it.messageHeaders
            },
        )

        return Greeting("Hello, ${message.name}(${sessionId})!")
    }

    @EventListener
    fun webSocketEstablished(event: WebSocketEstablishedEvent) {
        println("Web Socket Established: ${event.sessionId}")
    }

    @EventListener
    fun webSocketClosed(event: WebSocketClosedEvent) {
        println("Web Socket Closed: ${event.sessionId}")
    }

}
