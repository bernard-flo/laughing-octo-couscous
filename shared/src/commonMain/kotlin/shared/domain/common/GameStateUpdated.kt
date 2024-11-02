@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.common

import kotlinx.serialization.Serializable
import shared.domain.game.GameStateInfo
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class GameStateUpdated(
    val gameStateInfo: GameStateInfo,
)
