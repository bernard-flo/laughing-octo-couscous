@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.game

import kotlinx.serialization.Serializable
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
