@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.common

import kotlinx.serialization.Serializable
import shared.domain.player.PlayerName
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class ChatItem(
    val playerName: PlayerName,
    val chatMessage: ChatMessage,
)

@Serializable
data class ChatMessage(
    val value: String,
)
