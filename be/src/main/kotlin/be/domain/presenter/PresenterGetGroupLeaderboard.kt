package be.domain.presenter

import be.domain.common.SessionId
import be.domain.game.Game
import be.domain.player.PlayerSessionRegistry
import be.service.LoadPlayerService
import org.springframework.stereotype.Service
import shared.domain.game.GroupLeaderboardItem
import shared.domain.game.GroupScore
import shared.domain.game.Rank
import shared.domain.presenter.PresenterGetGroupLeaderboardResult


private val groupExclusion = listOf("테크본부", "이용권&정산개발 Unit")

@Service
class PresenterGetGroupLeaderboard(
    private val presenterSessionRegistry: PresenterSessionRegistry,
    private val game: Game,
    private val loadPlayerService: LoadPlayerService,
    private val playerSessionRegistry: PlayerSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
    ): PresenterGetGroupLeaderboardResult {

        presenterSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        val playerNameToGroupMap = loadPlayerService.playerInfoList.associateBy({ it.playerName }, { it.playerGroup })

        val registeredPlayerNames = playerSessionRegistry.getAll().map { it.playerName }

        val groupedItemMap = game.getLeaderboard()
            .filter { registeredPlayerNames.contains(it.playerName) }
            .groupBy { playerNameToGroupMap[it.playerName]!! }
            .map { (group, playerItems) -> group to playerItems }
            .filter { (group, _) -> groupExclusion.contains(group.value) == false }
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
