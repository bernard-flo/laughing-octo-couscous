package be.domain.presenter

import be.domain.common.SessionId
import be.domain.game.Game
import org.springframework.stereotype.Service
import shared.domain.presenter.PresenterGetLeaderboardResult

@Service
class PresenterGetLeaderboard(
    private val presenterSessionRegistry: PresenterSessionRegistry,
    private val game: Game,
) {

    operator fun invoke(
        sessionId: SessionId,
    ): PresenterGetLeaderboardResult {

        presenterSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        return PresenterGetLeaderboardResult(
            leaderboard = game.getLeaderboard(),
        )
    }

}
