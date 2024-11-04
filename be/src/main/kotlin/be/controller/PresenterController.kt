package be.controller

import be.domain.common.SessionId
import be.domain.presenter.PresenterEnter
import be.domain.presenter.PresenterGetGroupLeaderboard
import be.domain.presenter.PresenterGetLeaderboard
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult
import shared.domain.presenter.PresenterGetGroupLeaderboardResult
import shared.domain.presenter.PresenterGetLeaderboardResult


@Controller
private class PresenterController(
    private val presenterEnter: PresenterEnter,
    private val presenterGetLeaderboard: PresenterGetLeaderboard,
    private val presenterGetGroupLeaderboard: PresenterGetGroupLeaderboard,
) {

    @SendToUser("/topic/presenter/enter/result")
    @MessageMapping("/presenter/enter")
    fun presenterEnterApi(
        @Header("simpSessionId") sessionId: String,
        presenterEnterCommandPayload: PresenterEnterCommandPayload,
    ): PresenterEnterResult {

        return presenterEnter(
            sessionId = SessionId(sessionId),
            presenterEnterCommandPayload = presenterEnterCommandPayload,
        )
    }

    @SendToUser("/topic/presenter/getLeaderboard/result")
    @MessageMapping("/presenter/getLeaderboard")
    fun presenterGetLeaderboardApi(
        @Header("simpSessionId") sessionId: String,
    ): PresenterGetLeaderboardResult {

        return presenterGetLeaderboard(
            sessionId = SessionId(sessionId),
        )
    }

    @SendToUser("/topic/presenter/getGroupLeaderboard/result")
    @MessageMapping("/presenter/getGroupLeaderboard")
    fun presenterGetGroupLeaderboardApi(
        @Header("simpSessionId") sessionId: String,
    ): PresenterGetGroupLeaderboardResult {

        return presenterGetGroupLeaderboard(
            sessionId = SessionId(sessionId),
        )
    }

}
