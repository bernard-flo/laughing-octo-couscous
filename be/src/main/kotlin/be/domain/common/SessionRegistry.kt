package be.domain.common

open class SessionRegistry<SessionT : Session> {

    private val sessionMap = mutableMapOf<SessionId, SessionT>()

    fun add(session: SessionT) {

        sessionMap[session.sessionId] = session
    }

    fun get(sessionId: SessionId): SessionT? {

        return sessionMap[sessionId]
    }

    fun getAll(): List<SessionT> {

        return sessionMap.values.toList()
    }

}
