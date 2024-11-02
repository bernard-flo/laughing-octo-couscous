package fe.client.manager

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.IFrame
import fe.ext.stompjs.IMessage
import fe.ext.stompjs.PublishParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.common.GameStateUpdated
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult
import shared.domain.manager.ManagerPassword
import shared.stomp.TopicGameStateUpdated

class ManagerClient(
    private val onEntered: (ManagerEnterResult.Success) -> Unit,
    private val onGameStateUpdated: (GameStateUpdated) -> Unit,
) {

    private val stompClient: Client = createStompClient()

    fun enter(password: String) {

        if (stompClient.active) {
            doEnter(password)
        } else {
            stompClient.onConnect = createOnConnect(password)
            stompClient.activate()
        }
    }

    fun toAnsweringState() {

        stompClient.publish(
            PublishParams(
                destination = "/app/manager/toAnsweringState",
                body = null,
            )
        )
    }

    fun toAggregatedState() {

        stompClient.publish(
            PublishParams(
                destination = "/app/manager/toAggregatedState",
                body = null,
            )
        )
    }

    fun toNextQuiz() {

        stompClient.publish(
            PublishParams(
                destination = "/app/manager/toNextQuiz",
                body = null,
            )
        )
    }

    private fun doEnter(password: String) {

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

    private fun createOnConnect(password: String): (IFrame) -> Unit {

        return {
            console.log("connected")

            stompClient.subscribe("/user/topic/manager/enter/result", this::callbackPresenterEnterResult)
            stompClient.subscribe(TopicGameStateUpdated, this::callbackGameStateUpdated)

            doEnter(password)
        }
    }

    private fun callbackPresenterEnterResult(message: IMessage) {

        val result = Json.decodeFromString<ManagerEnterResult>(message.body)
        when (result) {

            is ManagerEnterResult.Success -> {
                console.log("enter succeeded")
                onEntered(result)
            }

            is ManagerEnterResult.Fail -> {
                console.log("enter failed")
            }
        }
    }

    private fun callbackGameStateUpdated(message: IMessage) {

        val gameStateUpdated = Json.decodeFromString<GameStateUpdated>(message.body)
        onGameStateUpdated(gameStateUpdated)
    }

}
