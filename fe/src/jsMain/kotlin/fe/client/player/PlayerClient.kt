package fe.client.player

import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import fe.ext.stompjs.StompConfig
import shared.message.PlayerEnterMessage
import shared.message.PlayerEnterResultMessage

class PlayerClient(
    private val onEntered: () -> Unit,
) {

    private val stompClient: Client

    init {
        stompClient = Client(
            StompConfig().apply {
                brokerURL = "ws://localhost:9080/stomp"
            }
        )
    }

    fun enter(playerName: String) {

        stompClient.onConnect = {
            console.log("connected")
            doEnter(playerName)
        }

        stompClient.activate()
    }


    private fun doEnter(playerName: String) {

        stompClient.subscribe("/user/topic/player/enter/result") { message ->

            val result = JSON.parse<PlayerEnterResultMessage>(message.body)
            if (result.success) {
                console.log("enter succeeded")
                onEntered()
            } else {
                console.log("enter failed")
            }
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/player/enter",
                body = JSON.stringify(
                    PlayerEnterMessage(
                        playerName = playerName,
                    )
                )
            )
        )
    }

}
