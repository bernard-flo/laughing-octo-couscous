package be.domain.presenter

import be.domain.common.Session
import be.domain.common.SessionId


data class PresenterSession(
    override val sessionId: SessionId,
) : Session
