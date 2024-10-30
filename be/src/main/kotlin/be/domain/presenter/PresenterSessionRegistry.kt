package be.domain.presenter

import be.domain.SessionId
import org.springframework.stereotype.Service

@Service
class PresenterSessionRegistry {

    private val presenterSessionMap: MutableMap<SessionId, PresenterSession> = mutableMapOf()

    fun add(presenterSession: PresenterSession) {

        presenterSessionMap[presenterSession.sessionId] = presenterSession
    }

    fun get(sessionId: SessionId): PresenterSession? {

        return presenterSessionMap[sessionId]
    }

}
