package fe.client.player

import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import fe.ext.stompjs.StompConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerName

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

            val result = Json.decodeFromString<PlayerEnterResult>(message.body)
            when (result) {

                is PlayerEnterResult.Success -> {
                    console.log("enter succeeded")
                    onEntered()
                }

                is PlayerEnterResult.Fail -> {
                    console.log("enter failed")
                }
            }
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/player/enter",
                body = Json.encodeToString(
                    PlayerEnterCommandPayload(
                        playerName = PlayerName(playerName)
                    )
                )
            )
        )
    }

}
