@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package shared.domain.presenter

import kotlinx.serialization.Serializable
import shared.domain.game.Leaderboard
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@Serializable
data class PresenterGetLeaderboardResult(
    val leaderboard: Leaderboard,
)
