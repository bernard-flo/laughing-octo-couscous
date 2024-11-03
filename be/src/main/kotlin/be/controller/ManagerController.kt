package be.controller

import be.domain.common.SessionId
import be.domain.manager.ManagerEnter
import be.domain.manager.ManagerResetGame
import be.domain.manager.ManagerToAggregatedState
import be.domain.manager.ManagerToAnsweredState
import be.domain.manager.ManagerToAnsweringState
import be.domain.manager.ManagerToNextQuiz
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.common.GameStateUpdated
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult
import shared.stomp.TopicGameStateUpdated


@Controller
private class ManagerController(
    private val managerEnter: ManagerEnter,
    private val managerToAnsweringState: ManagerToAnsweringState,
    private val managerToAnsweredState: ManagerToAnsweredState,
    private val managerToAggregatedState: ManagerToAggregatedState,
    private val managerToNextQuiz: ManagerToNextQuiz,
    private val managerResetGame: ManagerResetGame,
) {

    @SendToUser("/topic/manager/enter/result")
    @MessageMapping("/manager/enter")
    fun managerEnterApi(
        @Header("simpSessionId") sessionId: String,
        managerEnterCommandPayload: ManagerEnterCommandPayload,
    ): ManagerEnterResult {

        return managerEnter(
            sessionId = SessionId(sessionId),
            managerEnterCommandPayload = managerEnterCommandPayload,
        )
    }

    @SendTo(TopicGameStateUpdated)
    @MessageMapping("/manager/toAnsweringState")
    fun managerToAnsweringStateApi(
        @Header("simpSessionId") sessionId: String,
    ): GameStateUpdated {

        return managerToAnsweringState(
            sessionId = SessionId(sessionId),
        )
    }

    @SendTo(TopicGameStateUpdated)
    @MessageMapping("/manager/toAnsweredState")
    fun managerToAnsweredStateApi(
        @Header("simpSessionId") sessionId: String,
    ): GameStateUpdated {

        return managerToAnsweredState(
            sessionId = SessionId(sessionId),
        )
    }

    @SendTo(TopicGameStateUpdated)
    @MessageMapping("/manager/toAggregatedState")
    fun managerToAggregatedStateApi(
        @Header("simpSessionId") sessionId: String,
    ): GameStateUpdated {

        return managerToAggregatedState(
            sessionId = SessionId(sessionId),
        )
    }

    @SendTo(TopicGameStateUpdated)
    @MessageMapping("/manager/toNextQuiz")
    fun managerToNextQuizApi(
        @Header("simpSessionId") sessionId: String,
    ): GameStateUpdated {

        return managerToNextQuiz(
            sessionId = SessionId(sessionId),
        )
    }

    @SendTo(TopicGameStateUpdated)
    @MessageMapping("/manager/resetGame")
    fun managerResetGameApi(
        @Header("simpSessionId") sessionId: String,
    ): GameStateUpdated {

        return managerResetGame(
            sessionId = SessionId(sessionId),
        )
    }

}
