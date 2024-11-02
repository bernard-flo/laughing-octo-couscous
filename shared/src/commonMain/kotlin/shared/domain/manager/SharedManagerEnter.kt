@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.manager

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class ManagerEnterCommandPayload(
    val password: ManagerPassword,
)

@Serializable
sealed class ManagerEnterResult {

    @Serializable
    data object Success : ManagerEnterResult()

    @Serializable
    data object Fail : ManagerEnterResult()

}
