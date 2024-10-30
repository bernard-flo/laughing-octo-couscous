package be.domain.presenter

import be.domain.SessionId
import org.springframework.stereotype.Service

@Service
class PresenterEnter(
    private val presenterSessionRegistry: PresenterSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        presenterPassword: PresenterPassword,
    ): PresenterEnterResult {

        val presenterSession = PresenterSession(
            sessionId = sessionId,
        )
        presenterSessionRegistry.add(presenterSession)

        return PresenterEnterResult.Success
    }

}

sealed interface PresenterEnterResult {
    data object Success : PresenterEnterResult
    data object Fail : PresenterEnterResult
}
