@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.player

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PlayerEnterCommandPayload(
    val playerName: PlayerName,
)

@Serializable
sealed class PlayerEnterResult {

    @Serializable
    data object Success : PlayerEnterResult()

    @Serializable
    data object Fail : PlayerEnterResult()

}
