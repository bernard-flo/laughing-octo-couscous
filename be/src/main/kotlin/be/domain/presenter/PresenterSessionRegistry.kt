package be.domain.presenter

import be.domain.common.SessionRegistry
import org.springframework.stereotype.Service

@Service
class PresenterSessionRegistry : SessionRegistry<PresenterSession>()
