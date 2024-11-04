package be.service

import org.springframework.stereotype.Service
import shared.domain.player.PlayerGroup
import shared.domain.player.PlayerInfo
import shared.domain.player.PlayerName


private const val FILE_PATH_PLAYER_DATA = "resource/playerData.csv"

@Service
class LoadPlayerService {

    val playerInfoList by lazy { loadPlayerInfoList() }

    private fun loadPlayerInfoList(): List<PlayerInfo> {

        return readCsv(FILE_PATH_PLAYER_DATA)
            .map {
                PlayerInfo(
                    playerName = PlayerName(it[0]),
                    playerGroup = PlayerGroup(it[1]),
                )
            }
    }

}
