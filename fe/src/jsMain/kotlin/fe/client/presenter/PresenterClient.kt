package fe.client.presenter

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.IFrame
import fe.ext.stompjs.IMessage
import fe.ext.stompjs.PublishParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.common.ChatUpdated
import shared.domain.common.GameStateUpdated
import shared.domain.game.GameState
import shared.domain.game.Leaderboard
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult
import shared.domain.presenter.PresenterGetLeaderboardResult
import shared.domain.presenter.PresenterPassword
import shared.stomp.TopicGameStateUpdated

class PresenterClient(
    private val onEntered: () -> Unit,
    private val onEnterFailed: () -> Unit,
    private val onLeaderboardUpdated: (Leaderboard) -> Unit,
    private val onChatUpdated: (ChatUpdated) -> Unit,
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

    private fun doEnter(password: String) {

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

    private fun doGetLeaderboard() {

        stompClient.publish(
            PublishParams(
                destination = "/app/presenter/getLeaderboard",
                body = null,
            )
        )
    }

    private fun createOnConnect(password: String): (IFrame) -> Unit {

        return {
            console.log("connected")

            stompClient.subscribe("/user/topic/presenter/enter/result", this::callbackPlayerEnterResult)
            stompClient.subscribe(TopicGameStateUpdated, this::callbackGameStateUpdated)
            stompClient.subscribe("/user/topic/presenter/getLeaderboard/result", this::callbackGetLeaderboardResult)
            stompClient.subscribe("/topic/chatUpdated", this::callbackChatUpdated)

            doEnter(password)
        }
    }

    private fun callbackPlayerEnterResult(message: IMessage) {

        val result = Json.decodeFromString<PresenterEnterResult>(message.body)
        when (result) {

            PresenterEnterResult.Success -> {
                console.log("enter succeeded")
                onEntered()
                doGetLeaderboard()
            }

            PresenterEnterResult.Fail -> {
                console.log("enter failed")
                onEnterFailed()
            }
        }
    }

    private fun callbackGameStateUpdated(message: IMessage) {

        val gameStateUpdated = Json.decodeFromString<GameStateUpdated>(message.body)
        if (gameStateUpdated.gameStateInfo.gameState == GameState.Aggregated) {
            doGetLeaderboard()
        }
    }

    private fun callbackGetLeaderboardResult(message: IMessage) {

        val result = Json.decodeFromString<PresenterGetLeaderboardResult>(message.body)
        onLeaderboardUpdated(result.leaderboard)

        console.log("getLeaderboardResult", message)
    }

    private fun callbackChatUpdated(message: IMessage) {

        val result = Json.decodeFromString<ChatUpdated>(message.body)
        onChatUpdated(result)
    }

}
