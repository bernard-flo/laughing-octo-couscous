@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.common

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class ChatUpdated(
    val chatItem: ChatItem,
)
