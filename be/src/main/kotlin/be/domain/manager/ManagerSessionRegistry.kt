package be.domain.manager

import be.domain.common.SessionRegistry
import org.springframework.stereotype.Service

@Service
class ManagerSessionRegistry : SessionRegistry<ManagerSession>()
