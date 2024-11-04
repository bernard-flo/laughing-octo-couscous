package be.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import shared.domain.game.Score
import shared.domain.player.PlayerName
import java.io.File
import java.time.LocalDateTime


private const val backupDirectory = "backup/scoreMap/"
private const val backupFileNameFormat = "%s.json"

private const val restoreFileName = "resource/scoreMap.json"

@Service
class ScoreMapBackupService {

    fun backupScoreMap(scoreMap: Map<PlayerName, Score>) {

        val serializableScoreMap = scoreMap
            .map { (playerName, score) ->
                playerName.value to score.value
            }
            .toMap()

        val scoreMapJson = Json.encodeToString(serializableScoreMap)

        File(backupDirectory).mkdirs()
        File(backupDirectory + backupFileNameFormat.format(LocalDateTime.now())).writeText(scoreMapJson)
    }

    fun restoreScoreMap(): Map<PlayerName, Score>? {

        if (File(restoreFileName).exists() == false) {
            return null
        }

        val scoreMapJson = File(restoreFileName).readText()
        val serializableScoreMap = Json.decodeFromString<Map<String, Int>>(scoreMapJson)

        return serializableScoreMap
            .map { (playerName, score) ->
                PlayerName(playerName) to Score(score)
            }
            .toMap()
    }

}
