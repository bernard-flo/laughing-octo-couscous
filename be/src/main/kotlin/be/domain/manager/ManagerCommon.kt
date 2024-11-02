package be.domain.manager

import be.domain.common.Session
import be.domain.common.SessionId


data class ManagerSession(
    override val sessionId: SessionId,
) : Session
