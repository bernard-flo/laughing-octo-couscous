package be.domain.game

import org.springframework.stereotype.Service
import shared.domain.game.Answer
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.QuizIndex
import shared.domain.player.PlayerName

@Service
class Game {

    private val currentAnswerMap = mutableMapOf<PlayerName, Answer>()

    private var currentGameState = GameState.Ready
    private var currentQuizIndex = QuizIndex(0)

    fun getGameStateInfo(): GameStateInfo {

        return GameStateInfo(
            gameState = currentGameState,
            quizIndex = currentQuizIndex
        )
    }

    fun toAnsweringState() {

        check(currentGameState == GameState.Ready)

        currentGameState = GameState.Answering
    }

    fun toAggregatedState() {

        check(currentGameState == GameState.Answering)

        currentGameState = GameState.Aggregated
    }

    fun toNextQuiz() {

        check(currentGameState == GameState.Aggregated)

        synchronized(this) {
            currentQuizIndex = currentQuizIndex.createNext()
            currentGameState = GameState.Ready
        }
    }

    fun registerAnswer(playerName: PlayerName, answer: Answer): RegisterAnswerResult {

        if (currentGameState != GameState.Answering) {
            return RegisterAnswerResult.Fail
        }

        currentAnswerMap[playerName] = answer

        return RegisterAnswerResult.Success
    }

}


enum class RegisterAnswerResult {
    Success,
    Fail,
}
