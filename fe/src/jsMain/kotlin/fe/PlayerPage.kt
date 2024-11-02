package fe

import fe.client.player.PlayerClient
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import react.useState

internal val PlayerPage = FC<Props> {

    var entered by useState(false)

    val playerClientRef = useRef(
        PlayerClient(
            onEntered = { entered = true },
        )
    )

    div {
        +"Player"
        div {
            +entered.toString()
        }
        Button {
            variant = ButtonVariant.contained
            onClick = { playerClientRef.current!!.enter("Bernard") }
            +"Button"
        }
    }

}
