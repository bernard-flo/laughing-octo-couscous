package be.domain.presenter

import be.domain.common.SessionId
import org.springframework.stereotype.Service
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult

@Service
class PresenterEnter(
    private val presenterSessionRegistry: PresenterSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        presenterEnterCommandPayload: PresenterEnterCommandPayload,
    ): PresenterEnterResult {

        val presenterSession = PresenterSession(
            sessionId = sessionId,
        )
        presenterSessionRegistry.add(presenterSession)

        return PresenterEnterResult.Success
    }

}
