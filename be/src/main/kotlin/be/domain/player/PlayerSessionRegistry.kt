package be.domain.player

import be.domain.common.SessionRegistry
import org.springframework.stereotype.Service

@Service
class PlayerSessionRegistry : SessionRegistry<PlayerSession>()
