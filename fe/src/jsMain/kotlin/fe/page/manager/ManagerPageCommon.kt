package fe.page.manager

import fe.client.manager.ManagerClient
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.onChange
import react.useState
import web.html.InputType


internal external interface ManagerLoginComponentProps : Props {
    var managerClient: ManagerClient
}

internal val ManagerLoginComponent = FC<ManagerLoginComponentProps> { props ->

    var password by useState<String>("")

    val doClientEnter = { props.managerClient.enter(password) }

    div {
        div {
            +"Manager Login"
        }
        div {
            TextField {
                type = InputType.password
                variant = FormControlVariant.filled
                onChange = { password = it.target.asDynamic().value }
                onKeyUp = { if (it.key == "Enter") doClientEnter() }
            }
        }
        div {
            Button {
                variant = ButtonVariant.contained
                onClick = { doClientEnter() }
                +"Login"
            }
        }
    }
}
