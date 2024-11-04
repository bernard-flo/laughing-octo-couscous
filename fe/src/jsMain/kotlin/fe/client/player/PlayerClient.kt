package fe.client.player

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.IFrame
import fe.ext.stompjs.IMessage
import fe.ext.stompjs.PublishParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.common.ChatMessage
import shared.domain.common.GameStateUpdated
import shared.domain.game.Answer
import shared.domain.game.GameState
import shared.domain.game.PlayerQuizOutcome
import shared.domain.player.PlayerChatCommandPayload
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerGetQuizOutcomeResult
import shared.domain.player.PlayerName
import shared.domain.player.PlayerRegisterAnswerCommandPayload
import shared.domain.player.PlayerRegisterAnswerResult
import shared.stomp.TopicGameStateUpdated

class PlayerClient(
    private val onEntered: (PlayerEnterResult.Success) -> Unit,
    private val onEnterFailed: () -> Unit,
    private val onGameStateUpdated: (GameStateUpdated) -> Unit,
    private val onAnswerRegistered: (PlayerRegisterAnswerResult.Success) -> Unit,
    private val onRegisterAnswerFailed: () -> Unit,
    private val onQuizOutcome: (PlayerQuizOutcome) -> Unit,
) {

    private val stompClient: Client = createStompClient()

    fun enter(playerName: String) {

        if (stompClient.active) {
            doEnter(playerName)
        } else {
            stompClient.onConnect = createOnConnect(playerName)
            stompClient.activate()
        }
    }

    fun registerAnswer(answerValue: String) {

        stompClient.publish(
            PublishParams(
                destination = "/app/player/registerAnswer",
                body = Json.encodeToString(
                    PlayerRegisterAnswerCommandPayload(
                        answer = Answer(answerValue),
                    )
                )
            )
        )
    }

    fun chat(chatMessage: String) {

        stompClient.publish(
            PublishParams(
                destination = "/app/player/chat",
                body = Json.encodeToString(
                    PlayerChatCommandPayload(
                        chatMessage = ChatMessage(chatMessage),
                    )
                ),
            )
        )
    }

    private fun doEnter(playerName: String) {

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

    private fun doGetQuizOutcome() {

        stompClient.publish(
            PublishParams(
                destination = "/app/player/getQuizOutcome",
                body = null,
            )
        )
    }

    private fun createOnConnect(playerName: String): (IFrame) -> Unit {

        return {
            console.log("connected")

            stompClient.subscribe("/user/topic/player/enter/result", this::callbackPlayerEnterResult)
            stompClient.subscribe(TopicGameStateUpdated, this::callbackGameStateUpdated)
            stompClient.subscribe("/user/topic/player/registerAnswer/result", this::callbackRegisterAnswerResult)
            stompClient.subscribe("/user/topic/player/getQuizOutcome/result", this::callbackGetQuizOutcomeResult)

            doEnter(playerName)
        }
    }

    private fun callbackPlayerEnterResult(message: IMessage) {

        val result = Json.decodeFromString<PlayerEnterResult>(message.body)
        when (result) {

            is PlayerEnterResult.Success -> {
                console.log("enter succeeded")
                if (result.currentGameStateInfo.gameState == GameState.Aggregated) {
                    doGetQuizOutcome()
                }
                onEntered(result)
            }

            is PlayerEnterResult.Fail -> {
                console.log("enter failed")
                onEnterFailed()
            }
        }
    }

    private fun callbackGameStateUpdated(message: IMessage) {

        val gameStateUpdated = Json.decodeFromString<GameStateUpdated>(message.body)

        if (gameStateUpdated.gameStateInfo.gameState == GameState.Aggregated) {
            doGetQuizOutcome()
        }

        onGameStateUpdated(gameStateUpdated)
    }

    private fun callbackRegisterAnswerResult(message: IMessage) {

        val result = Json.decodeFromString<PlayerRegisterAnswerResult>(message.body)
        when (result) {

            is PlayerRegisterAnswerResult.Success -> {
                console.log("registerAnswer succeeded")
                onAnswerRegistered(result)
            }

            is PlayerRegisterAnswerResult.Fail -> {
                console.log("registerAnswer failed")
                onRegisterAnswerFailed()
            }
        }
    }

    private fun callbackGetQuizOutcomeResult(message: IMessage) {

        val result = Json.decodeFromString<PlayerGetQuizOutcomeResult>(message.body)

        when (result) {

            is PlayerGetQuizOutcomeResult.Success -> {
                console.log("getQuizOutcome succeeded")
                onQuizOutcome(result.playerQuizOutcome)
            }

            is PlayerGetQuizOutcomeResult.Fail -> {
                console.log("getQuizOutcome failed")
            }
        }
    }

}
