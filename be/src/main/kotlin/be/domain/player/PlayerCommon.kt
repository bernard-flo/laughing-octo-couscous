package be.domain.player


data class PlayerSession(
    val sessionId: SessionId,
    val playerName: PlayerName,
)

data class SessionId(
    val value: String,
)

data class PlayerName(
    val value: String,
)
