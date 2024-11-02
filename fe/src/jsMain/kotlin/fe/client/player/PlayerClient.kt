package fe.client.player

import fe.client.createStompClient
import fe.ext.stompjs.Client
import fe.ext.stompjs.IMessage
import fe.ext.stompjs.PublishParams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.common.GameStateUpdated
import shared.domain.game.Answer
import shared.domain.player.PlayerEnterCommandPayload
import shared.domain.player.PlayerEnterResult
import shared.domain.player.PlayerName
import shared.domain.player.PlayerRegisterAnswerCommandPayload
import shared.domain.player.PlayerRegisterAnswerResult
import shared.stomp.TopicGameStateUpdated

class PlayerClient(
    private val onEntered: (PlayerEnterResult.Success) -> Unit,
    private val onGameStateUpdated: (GameStateUpdated) -> Unit,
    private val onAnswerRegistered: (PlayerRegisterAnswerResult.Success) -> Unit,
) {

    private val stompClient: Client = createStompClient()

    fun enter(playerName: String) {

        stompClient.onConnect = {
            console.log("connected")
            stompClient.subscribe(TopicGameStateUpdated, this::callbackGameStateUpdated)
            stompClient.subscribe("/user/topic/player/registerAnswer/result", this::callbackRegisterAnswerResult)
            doEnter(playerName)
        }

        stompClient.activate()
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

    private fun doEnter(playerName: String) {

        stompClient.subscribe("/user/topic/player/enter/result") { message ->

            val result = Json.decodeFromString<PlayerEnterResult>(message.body)
            when (result) {

                is PlayerEnterResult.Success -> {
                    console.log("enter succeeded")
                    onEntered(result)
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

    private fun callbackGameStateUpdated(message: IMessage) {

        val gameStateUpdated = Json.decodeFromString<GameStateUpdated>(message.body)
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
            }
        }
    }

}
