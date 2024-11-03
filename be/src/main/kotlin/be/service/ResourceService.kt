package be.service

import be.domain.game.Quiz
import org.springframework.stereotype.Service
import shared.domain.game.Answer
import shared.domain.player.PlayerName
import java.io.File


private const val FILE_NAME_PLAYERS = "resource/players.txt"
private const val FILE_NAME_ANSWERS = "resource/answers.txt"

@Service
class ResourceService {

    fun loadPlayerNames(): List<PlayerName> {

        return File(FILE_NAME_PLAYERS).readLines()
            .filter { it.isNotBlank() }
            .map { PlayerName(it) }
    }

    fun loadQuizList(): List<Quiz> {

        return File(FILE_NAME_ANSWERS).readLines()
            .filter { it.isNotBlank() }
            .map { Quiz(Answer(it)) }
    }

}
