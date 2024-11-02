@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.presenter

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PresenterEnterCommandPayload(
    val password: PresenterPassword,
)

@Serializable
sealed class PresenterEnterResult {

    @Serializable
    data object Success : PresenterEnterResult()

    @Serializable
    data object Fail : PresenterEnterResult()

}
