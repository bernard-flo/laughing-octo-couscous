@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.game

import kotlinx.serialization.Serializable
import shared.domain.player.PlayerName
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
enum class GameState {
    Ready,
    Answering,
    Aggregated,
}

@Serializable
data class GameStateInfo(
    val gameState: GameState,
    val quizIndex: QuizIndex,
)

@Serializable
class QuizIndex(
    val value: Int,
) {

    fun createNext(): QuizIndex {
        return QuizIndex(
            value = this.value + 1,
        )
    }
}

@Serializable
data class Answer(
    val value: String,
)

@Serializable
data class Rank(
    val value: Int,
)

@Serializable
data class Score(
    val value: Int,
) {

    fun createPlus(amount: Int): Score {
        return Score(this.value + amount)
    }
}

@Serializable
data class LeaderboardItem(
    val rank: Rank,
    val playerName: PlayerName,
    val score: Score,
)

typealias Leaderboard = List<LeaderboardItem>
