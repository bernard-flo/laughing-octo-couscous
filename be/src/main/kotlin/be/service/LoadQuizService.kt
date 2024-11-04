package be.service

import be.domain.game.AnswerInfo
import be.domain.game.EarlyPlayerCount
import be.domain.game.Quiz
import mu.KotlinLogging
import org.springframework.stereotype.Service
import shared.domain.game.Answer
import shared.domain.game.Point


private val log = KotlinLogging.logger {}

private const val FILE_PATH_QUIZ_DATA = "resource/quizData.csv"

@Service
class LoadQuizService {

    fun loadQuizList(): List<Quiz> {

        val quizList = readCsv(FILE_PATH_QUIZ_DATA)
            .asSequence()
            .drop(1)
            .fold(mutableListOf<MutableList<CsvLine>>()) { acc, line ->
                if (line[0].isNotBlank()) {
                    acc.add(mutableListOf(line))
                } else {
                    acc.last().add(line)
                }
                acc
            }
            .map { groupedLines ->
                val firstLine = groupedLines.first()
                Quiz(
                    earlyPlayerCount = EarlyPlayerCount(firstLine[1].toInt()),
                    earlyPlayerPoint = Point(firstLine[2].toInt()),
                    basePoint = Point(firstLine[3].toInt()),
                    wrongPoint = Point(firstLine[4].toInt()),
                    answerInfoList = groupedLines.map { line ->
                        AnswerInfo(
                            answer = Answer(line[5]),
                            minusPoint = Point(line[6].toInt())
                        )
                    },
                )
            }

        quizList.forEach {
            log.info { it }
        }

        return quizList
    }

}
