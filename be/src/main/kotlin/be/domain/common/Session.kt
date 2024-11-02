package be.domain.common

interface Session {
    val sessionId: SessionId
}

data class SessionId(
    val value: String,
)
