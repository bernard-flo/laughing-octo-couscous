@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.player

import kotlinx.serialization.Serializable
import shared.domain.game.GameStateInfo
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PlayerEnterCommandPayload(
    val playerName: InputPlayerName,
)

@Serializable
sealed class PlayerEnterResult {

    @Serializable
    data class Success(
        val currentGameStateInfo: GameStateInfo,
    ) : PlayerEnterResult()

    @Serializable
    data object Fail : PlayerEnterResult()

}

@Serializable
data class InputPlayerName(
    val value: String,
)
