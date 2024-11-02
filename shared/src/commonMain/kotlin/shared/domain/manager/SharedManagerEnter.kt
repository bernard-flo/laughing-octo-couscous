@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.manager

import kotlinx.serialization.Serializable
import shared.domain.game.GameStateInfo
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class ManagerEnterCommandPayload(
    val password: ManagerPassword,
)

@Serializable
sealed class ManagerEnterResult {

    @Serializable
    data class Success(
        val currentGameStateInfo: GameStateInfo,
    ) : ManagerEnterResult()

    @Serializable
    data object Fail : ManagerEnterResult()

}
