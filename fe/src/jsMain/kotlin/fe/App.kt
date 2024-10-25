package fe

import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import shared.SharedTest

val App: FC<Props> = FC<Props> {

    h1 {
        +SharedTest.test()
    }

    Button {
        variant = ButtonVariant.contained
        +"Button"
    }

}
