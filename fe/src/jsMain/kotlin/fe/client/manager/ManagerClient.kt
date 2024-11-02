package fe.client.manager

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult
import shared.domain.manager.ManagerPassword

class ManagerClient(
    private val onEntered: () -> Unit,
) {

    private val stompClient: Client = createStompClient()

    fun enter(password: String) {

        stompClient.onConnect = {
            console.log("connected")
            doEnter(password)
        }

        stompClient.activate()
    }


    private fun doEnter(password: String) {

        stompClient.subscribe("/user/topic/manager/enter/result") { message ->

            val result = Json.decodeFromString<ManagerEnterResult>(message.body)
            when (result) {

                ManagerEnterResult.Success -> {
                    console.log("enter succeeded")
                    onEntered()
                }

                ManagerEnterResult.Fail -> {
                    console.log("enter failed")
                }
            }
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/manager/enter",
                body = Json.encodeToString(
                    ManagerEnterCommandPayload(
                        password = ManagerPassword(password),
                    )
                )
            )
        )
    }

}
