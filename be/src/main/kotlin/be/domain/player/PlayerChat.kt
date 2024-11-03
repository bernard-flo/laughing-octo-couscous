package be.domain.player

import be.domain.common.SessionId
import org.springframework.stereotype.Service
import shared.domain.common.ChatItem
import shared.domain.common.ChatUpdated
import shared.domain.player.PlayerChatCommandPayload

@Service
class PlayerChat(
    private val playerSessionRegistry: PlayerSessionRegistry,
) {

    operator fun invoke(
        sessionId: SessionId,
        payload: PlayerChatCommandPayload,
    ): ChatUpdated {

        val playerSession = playerSessionRegistry.get(sessionId) ?: throw IllegalStateException()

        return ChatUpdated(
            ChatItem(
                playerName = playerSession.playerName,
                chatMessage = payload.chatMessage,
            )
        )
    }

}
