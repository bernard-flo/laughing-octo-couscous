package fe.client

import fe.ext.stompjs.Client
import fe.ext.stompjs.StompConfig

fun createStompClient(): Client {

    return Client(
        StompConfig().apply {
            brokerURL = "ws://localhost:9080/stomp"
        }
    )
}
