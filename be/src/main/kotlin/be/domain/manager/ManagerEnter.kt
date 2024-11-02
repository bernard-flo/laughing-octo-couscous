package be.domain.manager

import be.domain.common.SessionId
import org.springframework.stereotype.Service
import shared.domain.manager.ManagerEnterCommandPayload
import shared.domain.manager.ManagerEnterResult

@Service
class ManagerEnter(
    private val managerSessionRegistry: ManagerSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        managerEnterCommandPayload: ManagerEnterCommandPayload,
    ): ManagerEnterResult {

        val managerSession = ManagerSession(
            sessionId = sessionId,
        )
        managerSessionRegistry.add(managerSession)

        return ManagerEnterResult.Success
    }

}
