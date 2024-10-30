package fe

import fe.client.presenter.PresenterClient
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import react.useState

internal val Presenter = FC<Props> {

    var entered by useState(false)

    val presenterClientRef = useRef(
        PresenterClient(
            onEntered = { entered = true },
        )
    )

    div {
        +"Presenter"
        div {
            +entered.toString()
        }
        Button {
            variant = ButtonVariant.contained
            onClick = { presenterClientRef.current!!.enter("Bernard") }
            +"Button"
        }
    }

}
