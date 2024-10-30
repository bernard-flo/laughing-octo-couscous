@file:OptIn(ExperimentalJsExport::class)

package shared.message

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
data class PresenterEnterMessage(
    val password: String,
)

@JsExport
data class PresenterEnterResultMessage(
    val success: Boolean,
)
