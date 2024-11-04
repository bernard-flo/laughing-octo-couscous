package fe.client.presenter

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.IFrame
import fe.ext.stompjs.IMessage
import js.objects.jso
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.common.ChatUpdated
import shared.domain.common.GameStateUpdated
import shared.domain.game.GameState
import shared.domain.game.GroupLeaderboard
import shared.domain.game.Leaderboard
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult
import shared.domain.presenter.PresenterGetGroupLeaderboardResult
import shared.domain.presenter.PresenterGetLeaderboardResult
import shared.domain.presenter.PresenterPassword
import shared.stomp.TopicGameStateUpdated
import web.storage.localStorage


private const val presenterPasswordStorageKey = "presenterPassword"

class PresenterClient(
    private val onEntered: () -> Unit,
    private val onEnterFailed: () -> Unit,
    private val onLeaderboardUpdated: (Leaderboard) -> Unit,
    private val onGroupLeaderboardUpdated: (GroupLeaderboard) -> Unit,
    private val onChatUpdated: (ChatUpdated) -> Unit,
) {

    private val stompClient: Client = createStompClient()

    private var enteredPassword: String? = null

    fun tryEnter() {

        val saved = localStorage.getItem(presenterPasswordStorageKey)
        if (saved != null) {
            enter(saved)
        }
    }

    fun enter(password: String) {

        enteredPassword = password

        if (stompClient.active) {
            doEnter(password)
        } else {
            stompClient.onConnect = createOnConnect(password)
            stompClient.activate()
        }
    }

    private fun doEnter(password: String) {

        stompClient.publish(
            jso {
                destination = "/app/presenter/enter"
                body = Json.encodeToString(
                    PresenterEnterCommandPayload(
                        password = PresenterPassword(password),
                    )
                )
            }
        )
    }

    private fun doGetLeaderboard() {

        stompClient.publish(
            jso {
                destination = "/app/presenter/getLeaderboard"
                body = null
            }
        )
    }

    private fun doGetGroupLeaderboard() {

        stompClient.publish(
            jso {
                destination = "/app/presenter/getGroupLeaderboard"
                body = null
            }
        )
    }

    private fun createOnConnect(password: String): (IFrame) -> Unit {

        return {
            console.log("connected")

            stompClient.subscribe("/user/topic/presenter/enter/result", this::callbackPlayerEnterResult)
            stompClient.subscribe(TopicGameStateUpdated, this::callbackGameStateUpdated)
            stompClient.subscribe("/user/topic/presenter/getLeaderboard/result", this::callbackGetLeaderboardResult)
            stompClient.subscribe(
                "/user/topic/presenter/getGroupLeaderboard/result",
                this::callbackGetGroupLeaderboardResult
            )
            stompClient.subscribe("/topic/chatUpdated", this::callbackChatUpdated)

            doEnter(password)
        }
    }

    private fun callbackPlayerEnterResult(message: IMessage) {

        val result = Json.decodeFromString<PresenterEnterResult>(message.body)
        when (result) {

            PresenterEnterResult.Success -> {
                console.log("enter succeeded")

                localStorage.setItem(presenterPasswordStorageKey, enteredPassword!!)

                onEntered()
                doGetLeaderboard()
                doGetGroupLeaderboard()
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

    private fun callbackGetGroupLeaderboardResult(message: IMessage) {

        val result = Json.decodeFromString<PresenterGetGroupLeaderboardResult>(message.body)
        onGroupLeaderboardUpdated(result.groupLeaderboard)

        console.log("getGroupLeaderboardResult", message)
    }

    private fun callbackChatUpdated(message: IMessage) {

        val result = Json.decodeFromString<ChatUpdated>(message.body)
        onChatUpdated(result)
    }

}
