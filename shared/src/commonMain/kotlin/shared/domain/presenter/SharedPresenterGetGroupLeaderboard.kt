@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.presenter

import kotlinx.serialization.Serializable
import shared.domain.game.GroupLeaderboard
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PresenterGetGroupLeaderboardResult(
    val groupLeaderboard: GroupLeaderboard,
)
