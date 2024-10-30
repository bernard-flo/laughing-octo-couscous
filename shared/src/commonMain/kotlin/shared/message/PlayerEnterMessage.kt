@file:OptIn(ExperimentalJsExport::class)

package shared.message

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
data class PlayerEnterMessage(
    val playerName: String,
)

@JsExport
data class PlayerEnterResultMessage(
    val success: Boolean,
)
