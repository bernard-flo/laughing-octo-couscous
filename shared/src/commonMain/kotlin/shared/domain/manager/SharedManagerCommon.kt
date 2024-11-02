@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.manager

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class ManagerPassword(
    val value: String,
)
