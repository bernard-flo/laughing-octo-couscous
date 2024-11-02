package fe

import fe.client.presenter.PresenterClient
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.onChange
import react.useRef
import react.useState
import shared.domain.game.Leaderboard
import web.html.InputType

internal val PresenterPage = FC<Props> {

    var entered by useState(false)
    var leaderboard by useState<Leaderboard>(listOf())

    val presenterClientRef = useRef(
        PresenterClient(
            onEntered = {
                entered = true
            },
            onLeaderboardUpdated = {
                leaderboard = it
            },
        )
    )

    div {
        if (entered) {
            PresenterGameComponent {
                this.leaderboard = leaderboard
            }
        } else {
            PresenterLoginComponent {
                this.presenterClient = presenterClientRef.current!!
            }
        }
    }

}


private external interface PresenterGameComponentProps : Props {
    var leaderboard: Leaderboard
}

private val PresenterGameComponent = FC<PresenterGameComponentProps> { props ->

    div {

        for (leaderboardItem in props.leaderboard) {
            val rankValue = leaderboardItem.rank.value
            val playerNameValue = leaderboardItem.playerName.value
            val scoreValue = leaderboardItem.score.value

            div {
                +"${rankValue}등 ${playerNameValue} (${scoreValue}점)"
            }
        }
    }
}


private external interface PresenterLoginComponentProps : Props {
    var presenterClient: PresenterClient
}

private val PresenterLoginComponent = FC<PresenterLoginComponentProps> { props ->

    var password by useState<String>("")

    val doClientEnter = { props.presenterClient.enter(password) }

    div {
        div {
            +"Presenter Login"
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
