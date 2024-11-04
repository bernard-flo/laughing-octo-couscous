package fe.page.presenter

import fe.client.presenter.PresenterClient
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.FormControlVariant
import mui.material.TextField
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.StateSetter
import react.dom.onChange
import react.useState
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.px
import web.html.InputType


internal external interface PresenterLoginComponentProps : Props {
    var presenterClient: PresenterClient
    var enterFailed: Boolean
    var setEnterFailed: StateSetter<Boolean>
}

internal val PresenterLoginComponent = FC<PresenterLoginComponentProps> { props ->

    var password by useState<String>("")

    val doClientEnter = { props.presenterClient.enter(password) }

    Box {
        sx {
            display = Display.grid
            justifyContent = JustifyContent.center
            marginTop = 100.px
            gap = 40.px
        }
        Typography {
            variant = TypographyVariant.h2
            +"Presenter"
        }
        TextField {
            type = InputType.password
            variant = FormControlVariant.outlined
            label = ReactNode("비밀번호")
            error = props.enterFailed
            helperText = if (props.enterFailed) ReactNode("비밀번호를 정확하게 입력해주세요") else null
            onChange = {
                password = it.target.asDynamic().value
                props.setEnterFailed(false)
            }
            onKeyUp = { if (it.key == "Enter") doClientEnter() }
        }
        Button {
            variant = ButtonVariant.contained
            onClick = { doClientEnter() }
            +"Login"
        }
    }
}
