package be.domain.presenter

import be.domain.common.SessionId
import be.domain.game.Game
import be.service.LoadPlayerService
import org.springframework.stereotype.Service
import shared.domain.game.GroupLeaderboardItem
import shared.domain.game.GroupScore
import shared.domain.game.Rank
import shared.domain.presenter.PresenterGetGroupLeaderboardResult

@Service
class PresenterGetGroupLeaderboard(
    private val presenterSessionRegistry: PresenterSessionRegistry,
    private val game: Game,
    private val loadPlayerService: LoadPlayerService,
) {

    operator fun invoke(
        sessionId: SessionId,
    ): PresenterGetGroupLeaderboardResult {

        presenterSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        val playerNameToGroupMap = loadPlayerService.playerInfoList.associateBy({ it.playerName }, { it.playerGroup })

        val groupedItemMap = game.getLeaderboard()
            .groupBy { playerNameToGroupMap[it.playerName]!! }
            .map { (group, playerItems) -> group to playerItems }
            .toMap()

        val groupScoreMap = groupedItemMap
            .map { (group, playerItems) ->
                val average = playerItems.map { it.score.value }.average()
                group to GroupScore(average)
            }
            .toMap()

        val groupLeaderboard = groupScoreMap.entries
            .sortedByDescending { (_, score) -> score.value }
            .mapIndexed { index, (group, score) ->
                GroupLeaderboardItem(
                    rank = Rank(index + 1),
                    group = group,
                    groupScore = score,
                    playerItemList = groupedItemMap[group] ?: listOf(),
                )
            }

        return PresenterGetGroupLeaderboardResult(
            groupLeaderboard = groupLeaderboard
        )
    }

}
