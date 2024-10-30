package fe.client.presenter

import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import fe.ext.stompjs.StompConfig
import shared.message.PresenterEnterMessage
import shared.message.PresenterEnterResultMessage

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

            val result = JSON.parse<PresenterEnterResultMessage>(message.body)
            if (result.success) {
                console.log("enter succeeded")
                onEntered()
            } else {
                console.log("enter failed")
            }
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/presenter/enter",
                body = JSON.stringify(
                    PresenterEnterMessage(
                        password = password,
                    )
                )
            )
        )
    }

}
