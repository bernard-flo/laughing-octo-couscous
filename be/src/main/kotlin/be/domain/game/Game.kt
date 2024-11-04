package be.domain.game

import be.service.ResourceService
import org.springframework.stereotype.Service
import shared.domain.game.Answer
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.Leaderboard
import shared.domain.game.LeaderboardItem
import shared.domain.game.PlayerQuizOutcome
import shared.domain.game.QuizIndex
import shared.domain.game.QuizOutcomeType
import shared.domain.game.Rank
import shared.domain.game.Score
import shared.domain.player.PlayerName

@Service
class Game(
    resourceService: ResourceService,
) {

    private val quizList: List<Quiz> = resourceService.loadQuizList()

    private val scoreMap = mutableMapOf<PlayerName, Score>()
    private val leaderboard = mutableListOf<LeaderboardItem>()
    private val currentAnswerMap = mutableMapOf<PlayerName, Answer>()

    private var currentGameState = GameState.Ready
    private var currentQuizIndex = QuizIndex(0)

    fun getGameStateInfo(): GameStateInfo = synchronized(this) {

        return GameStateInfo(
            gameState = currentGameState,
            quizIndex = currentQuizIndex
        )
    }

    fun getPlayerQuizOutcome(playerName: PlayerName): PlayerQuizOutcome = synchronized(this) {

        check(currentGameState == GameState.Aggregated)

        val isCorrect = currentAnswerMap[playerName] == quizList[currentQuizIndex.value].answer

        return PlayerQuizOutcome(
            type = if (isCorrect) QuizOutcomeType.Correct else QuizOutcomeType.Incorrect,
            score = scoreMap[playerName] ?: Score(0)
        )
    }

    fun getLeaderboard(): Leaderboard = synchronized(this) {

        return leaderboard
    }

    fun toAnsweringState() = synchronized(this) {

        check(currentGameState == GameState.Ready)

        currentGameState = GameState.Answering
    }

    fun toAnsweredState() = synchronized(this) {

        check(currentGameState == GameState.Answering)

        currentGameState = GameState.Answered
    }

    fun toAggregatedState() = synchronized(this) {

        check(currentGameState == GameState.Answered)

        aggregate()
        currentGameState = GameState.Aggregated
    }

    fun toNextQuiz() = synchronized(this) {

        check(currentGameState == GameState.Aggregated)

        currentQuizIndex = currentQuizIndex.createNext()

        currentGameState = if (currentQuizIndex.value < quizList.size) {
            GameState.Ready
        } else {
            GameState.Finished
        }

        currentAnswerMap.clear()
    }

    fun resetGame() = synchronized(this) {

        scoreMap.clear()
        leaderboard.clear()
        currentAnswerMap.clear()

        currentGameState = GameState.Ready
        currentQuizIndex = QuizIndex(0)
    }

    fun registerAnswer(playerName: PlayerName, answer: Answer): RegisterAnswerResult = synchronized(this) {

        if (currentGameState != GameState.Answering) {
            return RegisterAnswerResult.Fail
        }

        currentAnswerMap[playerName] = answer

        return RegisterAnswerResult.Success
    }

    private fun aggregate() {

        updateScoreMap()
        updateLeaderboard()
    }

    private fun updateScoreMap() {

        val quizAnswer = quizList[currentQuizIndex.value].answer

        for ((playerName, playerAnswer) in currentAnswerMap) {
            if (playerAnswer == quizAnswer) {
                val prevScore = scoreMap[playerName] ?: Score(0)
                val newScore = prevScore.createPlus(1)
                scoreMap[playerName] = newScore
            }
        }
    }

    private fun updateLeaderboard() {

        leaderboard.clear()

        val newList = scoreMap.entries
            .sortedByDescending { (_, score) -> score.value }
            .mapIndexed { index, (playerName, score) ->
                LeaderboardItem(
                    rank = Rank(index + 1),
                    playerName = playerName,
                    score = score,
                )
            }
        leaderboard.addAll(newList)
    }

}


data class Quiz(
    val answer: Answer,
)

enum class RegisterAnswerResult {
    Success,
    Fail,
}
