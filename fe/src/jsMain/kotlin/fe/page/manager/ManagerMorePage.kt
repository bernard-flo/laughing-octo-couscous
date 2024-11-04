package fe.page.manager

import fe.client.manager.ManagerClient
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useRef
import react.useState
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo

internal val ManagerMorePage = FC<Props> {

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

    useEffect(null) { managerClientRef.current!!.tryEnter() }

    div {
        if (entered) {
            ManagerMoreGameComponent {
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


private external interface ManagerMoreGameComponentProps : Props {
    var managerClient: ManagerClient
    var currentGameStateInfo: GameStateInfo
}

private val ManagerMoreGameComponent = FC<ManagerMoreGameComponentProps> { props ->

    val quizNo = props.currentGameStateInfo.quizIndex.value + 1
    val gameState = props.currentGameStateInfo.gameState

    div {
        div {
            if (gameState == GameState.Finished) {
                +"퀴즈가 끝났습니다"
            } else {
                +"${quizNo}번 문제"
            }
        }
        div {
            Button {
                variant = ButtonVariant.contained
                onClick = { props.managerClient.resetGame() }
                +"초기화"
            }
        }
    }
}
