package fe

import fe.client.manager.ManagerClient
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.FormControlVariant
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import react.useState
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo

internal val ManagerPage = FC<Props> {

    var entered by useState(false)
    var currentGameStateInfo by useState<GameStateInfo?>(null)

    val managerClientRef = useRef(
        ManagerClient(
            onEntered = {
                entered = true
                currentGameStateInfo = it.currentGameStateInfo
            },
            onGameStateUpdated = {
                currentGameStateInfo = it.gameStateInfo
            },
        )
    )

    div {
        if (entered) {
            ManagerGameComponent {
                this.managerClient = managerClientRef.current!!
                this.currentGameStateInfo = currentGameStateInfo!!
            }
        } else {
            ManagerLoginComponent {
                this.managerClient = managerClientRef.current!!
            }
        }
    }

}


private external interface ManagerGameComponentProps : Props {
    var managerClient: ManagerClient
    var currentGameStateInfo: GameStateInfo
}

private val ManagerGameComponent = FC<ManagerGameComponentProps> { props ->

    val quizNo = props.currentGameStateInfo.quizIndex.value + 1

    div {
        div {
            +"${quizNo}번 문제"
        }

        when (props.currentGameStateInfo.gameState) {

            GameState.Ready -> {
                div {
                    Button {
                        variant = ButtonVariant.contained
                        onClick = { props.managerClient.toAnsweringState() }
                        +"입력 시작!"
                    }
                }
            }

            GameState.Answering -> {
                div {
                    Button {
                        variant = ButtonVariant.contained
                        onClick = { props.managerClient.toAggregatedState() }
                        +"입력 마감!"
                    }
                }
            }

            GameState.Aggregated -> {
                div {
                    Button {
                        variant = ButtonVariant.contained
                        onClick = { props.managerClient.toNextQuiz() }
                        +"다음 문제!"
                    }
                }
            }
        }
    }
}


private external interface ManagerLoginComponentProps : Props {
    var managerClient: ManagerClient
}

private val ManagerLoginComponent = FC<ManagerLoginComponentProps> { props ->

    div {
        div {
            +"Manager Login"
        }
        div {
            TextField {
                variant = FormControlVariant.filled
            }
        }
        div {
            Button {
                variant = ButtonVariant.contained
                onClick = { props.managerClient.enter("Bernard") }
                +"Login"
            }
        }
    }
}
