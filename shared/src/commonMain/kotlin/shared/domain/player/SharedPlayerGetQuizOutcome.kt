@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.player

import kotlinx.serialization.Serializable
import shared.domain.game.PlayerQuizOutcome
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
sealed class PlayerGetQuizOutcomeResult {

    @Serializable
    data class Success(
        val playerQuizOutcome: PlayerQuizOutcome,
    ) : PlayerGetQuizOutcomeResult()

    @Serializable
    data object Fail : PlayerGetQuizOutcomeResult()

}
