@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.presenter

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PresenterPassword(
    val value: String,
)
