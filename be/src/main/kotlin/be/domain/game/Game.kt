package be.domain.game

import be.service.LoadQuizService
import be.service.ScoreMapBackupService
import org.springframework.stereotype.Service
import shared.domain.game.Answer
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.Leaderboard
import shared.domain.game.LeaderboardItem
import shared.domain.game.PlayerQuizOutcome
import shared.domain.game.Point
import shared.domain.game.QuizIndex
import shared.domain.game.QuizOutcomeType
import shared.domain.game.Rank
import shared.domain.game.Score
import shared.domain.player.PlayerGetQuizOutcomeResult
import shared.domain.player.PlayerName

@Service
class Game(
    private val scoreMapBackupService: ScoreMapBackupService,
    loadQuizService: LoadQuizService,
) {

    private val quizList: List<Quiz> = loadQuizService.loadQuizList()

    private val scoreMap = mutableMapOf<PlayerName, Score>()
    private val leaderboard = mutableListOf<LeaderboardItem>()
    private val currentAnswerMap = linkedMapOf<PlayerName, Answer>()
    private val currentQuizOutcomeInfoMap = mutableMapOf<PlayerName, QuizOutcomeInfo>()

    private var currentGameState = GameState.Ready
    private var currentQuizIndex = QuizIndex(0)

    init {
        scoreMapBackupService.restoreScoreMap()?.let {
            scoreMap.putAll(it)
            updateLeaderboard()
        }
    }

    fun getGameStateInfo(): GameStateInfo = synchronized(this) {

        return GameStateInfo(
            gameState = currentGameState,
            quizIndex = currentQuizIndex
        )
    }

    fun getPlayerQuizOutcome(playerName: PlayerName): PlayerGetQuizOutcomeResult = synchronized(this) {

        check(currentGameState == GameState.Aggregated)

        val outcomeInfo = currentQuizOutcomeInfoMap[playerName] ?: return PlayerGetQuizOutcomeResult.Fail
        val score = scoreMap[playerName] ?: return PlayerGetQuizOutcomeResult.Fail

        return PlayerGetQuizOutcomeResult.Success(
            PlayerQuizOutcome(
                type = outcomeInfo.type,
                point = outcomeInfo.point,
                score = score,
            )
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
        currentQuizOutcomeInfoMap.clear()
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

        currentAnswerMap.putLast(playerName, answer)

        return RegisterAnswerResult.Success
    }

    private fun aggregate() {

        updateQuizOutcome()
        updateScoreMap()
        updateLeaderboard()

        scoreMapBackupService.backupScoreMap(scoreMap)
    }

    private fun updateQuizOutcome() {

        val currentQuiz = quizList[currentQuizIndex.value]

        var earlyPlayerOrder = 0
        for ((playerName, playerAnswer) in currentAnswerMap) {

            val outcomeInfo = currentQuiz.checkAnswer(playerAnswer, earlyPlayerOrder)
            if (outcomeInfo.type == QuizOutcomeType.Early) {
                earlyPlayerOrder += 1
            }
            currentQuizOutcomeInfoMap[playerName] = outcomeInfo
        }
    }

    private fun updateScoreMap() {

        for ((playerName, outcomeInfo) in currentQuizOutcomeInfoMap) {
            val prevScore = scoreMap[playerName] ?: Score(0)
            val newScore = prevScore.createPlus(outcomeInfo.point)
            scoreMap[playerName] = newScore
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
    val earlyPlayerCount: EarlyPlayerCount,
    val earlyPlayerPoint: Point,
    val basePoint: Point,
    val wrongPoint: Point,
    val answerInfoList: List<AnswerInfo>,
) {

    fun checkAnswer(playerAnswer: Answer, earlyPlayerOrder: Int): QuizOutcomeInfo {

        val matchedAnswerInfo = answerInfoList.find { it.answer == playerAnswer }
        return if (matchedAnswerInfo != null) {
            if (matchedAnswerInfo.minusPoint.isZero()) {
                if (earlyPlayerOrder < earlyPlayerCount.value) {
                    QuizOutcomeInfo(QuizOutcomeType.Early, earlyPlayerPoint)
                } else {
                    QuizOutcomeInfo(QuizOutcomeType.Correct, basePoint)
                }
            } else {
                QuizOutcomeInfo(QuizOutcomeType.Partial, basePoint.createPlus(matchedAnswerInfo.minusPoint))
            }
        } else {
            QuizOutcomeInfo(QuizOutcomeType.Incorrect, wrongPoint)
        }
    }

}

data class EarlyPlayerCount(
    val value: Int,
)

data class AnswerInfo(
    val answer: Answer,
    val minusPoint: Point,
)

data class QuizOutcomeInfo(
    val type: QuizOutcomeType,
    val point: Point,
)

enum class RegisterAnswerResult {
    Success,
    Fail,
}
