package fe.client

import fe.ext.stompjs.Client
import fe.ext.stompjs.StompConfig

external val STOMP_BROKER_URL: String

fun createStompClient(): Client {

    return Client(
        StompConfig().apply {
            brokerURL = STOMP_BROKER_URL
        }
    )
}
