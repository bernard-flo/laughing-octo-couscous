package fe

import fe.client.manager.ManagerClient
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import react.useState

internal val Manager = FC<Props> {

    var entered by useState(false)

    val managerClientRef = useRef(
        ManagerClient(
            onEntered = { entered = true },
        )
    )

    div {
        +"Manager"
        div {
            +entered.toString()
        }
        Button {
            variant = ButtonVariant.contained
            onClick = { managerClientRef.current!!.enter("Bernard") }
            +"Button"
        }
    }

}
