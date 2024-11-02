package fe.client.presenter

import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import fe.ext.stompjs.StompConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult
import shared.domain.presenter.PresenterPassword

class PresenterClient(
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

    fun enter(password: String) {

        stompClient.onConnect = {
            console.log("connected")
            doEnter(password)
        }

        stompClient.activate()
    }


    private fun doEnter(password: String) {

        stompClient.subscribe("/user/topic/presenter/enter/result") { message ->

            val result = Json.decodeFromString<PresenterEnterResult>(message.body)
            when (result) {

                PresenterEnterResult.Success -> {
                    console.log("enter succeeded")
                    onEntered()
                }

                PresenterEnterResult.Fail -> {
                    console.log("enter failed")
                }
            }
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/presenter/enter",
                body = Json.encodeToString(
                    PresenterEnterCommandPayload(
                        password = PresenterPassword(password),
                    )
                )
            )
        )
    }

}
