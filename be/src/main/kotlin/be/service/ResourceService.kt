package be.service

import org.springframework.stereotype.Service
import shared.domain.player.PlayerName
import java.io.File


private const val FILE_NAME_PLAYERS = "resource/players.txt"

@Service
class ResourceService {

    fun loadPlayerNames(): List<PlayerName> {

        return File(FILE_NAME_PLAYERS).readLines()
            .filter { it.isNotBlank() }
            .map { PlayerName(it) }
    }

}
