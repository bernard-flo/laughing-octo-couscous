@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.player

import kotlinx.serialization.Serializable
import shared.domain.game.Answer
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PlayerRegisterAnswerCommandPayload(
    val answer: Answer,
)

@Serializable
sealed class PlayerRegisterAnswerResult {

    @Serializable
    data class Success(
        val registeredAnswer: Answer,
    ) : PlayerRegisterAnswerResult()

    @Serializable
    data object Fail : PlayerRegisterAnswerResult()

}
