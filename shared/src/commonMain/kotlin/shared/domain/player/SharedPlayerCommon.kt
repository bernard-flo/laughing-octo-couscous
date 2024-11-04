@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.player

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PlayerName(
    val value: String,
)

@Serializable
data class PlayerGroup(
    val value: String,
)

@Serializable
data class PlayerInfo(
    val playerName: PlayerName,
    val playerGroup: PlayerGroup,
)
