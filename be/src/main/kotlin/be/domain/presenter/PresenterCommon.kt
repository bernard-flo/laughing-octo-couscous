package be.domain.presenter

import be.domain.SessionId


data class PresenterSession(
    val sessionId: SessionId,
)

data class PresenterPassword(
    val value: String,
)
