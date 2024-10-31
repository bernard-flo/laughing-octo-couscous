@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.message

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

data class PlayerEnterMessage(
    val playerName: String,
)

data class PlayerEnterResultMessage(
    val success: Boolean,
)
