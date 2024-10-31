@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.message

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

data class PresenterEnterMessage(
    val password: String,
)

data class PresenterEnterResultMessage(
    val success: Boolean,
)
