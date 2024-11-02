package be.domain.presenter

import be.domain.common.SessionId
import be.service.EnvService
import org.springframework.stereotype.Service
import shared.domain.presenter.PresenterEnterCommandPayload
import shared.domain.presenter.PresenterEnterResult

@Service
class PresenterEnter(
    private val presenterSessionRegistry: PresenterSessionRegistry,
    private val envService: EnvService,
) {

    operator fun invoke(
        sessionId: SessionId,
        presenterEnterCommandPayload: PresenterEnterCommandPayload,
    ): PresenterEnterResult {

        if (envService.matchPresenterPassword(presenterEnterCommandPayload.password) == false) {
            return PresenterEnterResult.Fail
        }

        val presenterSession = PresenterSession(
            sessionId = sessionId,
        )
        presenterSessionRegistry.add(presenterSession)

        return PresenterEnterResult.Success
    }

}
