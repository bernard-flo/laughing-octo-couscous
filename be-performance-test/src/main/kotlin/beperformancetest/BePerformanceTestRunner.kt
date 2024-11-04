package beperformancetest

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.time.LocalDateTime


private const val url = "wss://flovs.link/stomp"

private const val agentCount = 100

@Component
private class BePerformanceTestRunner(
    private val taskScheduler: TaskScheduler,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        (1..agentCount).forEach {
            runOne(it, taskScheduler)
        }
    }
}


private fun onEntered(session: StompSession, id: Int) {

    repeat(10) {
        session.send(
            "/app/player/chat",
            """{"chatMessage":{"value":"${id}-${LocalDateTime.now()}"}}"""
        )
        Thread.sleep(1000)
    }
}

private fun runOne(id: Int, taskScheduler: TaskScheduler) {

    val stompClient = WebSocketStompClient(StandardWebSocketClient()).also {
        it.messageConverter = messageConverter
        it.taskScheduler = taskScheduler
    }

    stompClient.connectAsync(url, object : StompSessionHandlerAdapter() {

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {

            session.subscribe("/user/topic/player/enter/result", object : StompSessionHandlerAdapter() {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    onEntered(session, id)
                }
            })

            session.send("/app/player/enter", """{"playerName":{"value":"bernard"}}""")
        }

        override fun handleException(
            session: StompSession,
            command: StompCommand?,
            headers: StompHeaders,
            payload: ByteArray,
            exception: Throwable
        ) {
            exception.printStackTrace()
        }
    })
}

private val messageConverter = object : MessageConverter {

    override fun fromMessage(message: Message<*>, targetClass: Class<*>): Any {
        return if (message.payload is ByteArray) {
            String(message.payload as ByteArray)
        } else {
            message.payload
        }
    }

    override fun toMessage(payload: Any, headers: MessageHeaders?): Message<*> {

        val convertedPayload = if (payload is String) {
            payload.toByteArray()
        } else {
            payload
        }
        return MessageBuilder.createMessage(convertedPayload, headers ?: MessageHeaders(mapOf()))
    }
}
