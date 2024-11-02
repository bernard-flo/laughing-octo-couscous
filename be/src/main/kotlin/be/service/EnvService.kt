package be.service

import org.springframework.stereotype.Service
import shared.domain.manager.ManagerPassword
import shared.domain.presenter.PresenterPassword

@Service
class EnvService {

    fun matchPresenterPassword(presenterPassword: PresenterPassword): Boolean {

        val envPassword = System.getenv("PRESENTER_PASSWORD")
        return presenterPassword.value == envPassword
    }

    fun matchManagerPassword(managerPassword: ManagerPassword): Boolean {

        val envPassword = System.getenv("MANAGER_PASSWORD")
        return managerPassword.value == envPassword
    }

}
