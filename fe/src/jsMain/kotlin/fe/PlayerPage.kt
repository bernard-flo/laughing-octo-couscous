package fe

import fe.client.player.PlayerClient
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
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.PlayerQuizOutcome
import shared.domain.game.QuizOutcomeType

internal val PlayerPage = FC<Props> {

    var entered by useState(false)
    var enterFailed by useState(false)
    var currentGameStateInfo by useState<GameStateInfo?>(null)
    var registeredAnswer by useState<String?>(null)
    var quizOutcome by useState<PlayerQuizOutcome?>(null)

    val playerClientRef = useRef(
        PlayerClient(
            onEntered = {
                entered = true
                currentGameStateInfo = it.currentGameStateInfo
            },
            onEnterFailed = {
                enterFailed = true
            },
            onGameStateUpdated = {
                currentGameStateInfo = it.gameStateInfo
            },
            onAnswerRegistered = {
                registeredAnswer = it.registeredAnswer.value
            },
            onRegisterAnswerFailed = {
                registeredAnswer = "(잠시 후에 입력해주세요)"
            },
            onQuizOutcome = {
                quizOutcome = it
            },
        )
    )

    div {
        if (entered) {
            PlayerGameComponent {
                this.playerClient = playerClientRef.current!!
                this.currentGameStateInfo = currentGameStateInfo!!
                this.registeredAnswer = registeredAnswer
                this.quizOutcome = quizOutcome
            }
        } else {
            PlayerLoginComponent {
                this.playerClient = playerClientRef.current!!
                this.enterFailed = enterFailed
            }
        }
    }

}


private external interface PlayerGameComponentProps : Props {
    var playerClient: PlayerClient
    var currentGameStateInfo: GameStateInfo
    var registeredAnswer: String?
    var quizOutcome: PlayerQuizOutcome?
}

private val PlayerGameComponent = FC<PlayerGameComponentProps> { props ->

    val quizNo = props.currentGameStateInfo.quizIndex.value + 1
    val gameState = props.currentGameStateInfo.gameState
    val score = props.quizOutcome?.score?.value ?: 0
    val isCorrect = props.quizOutcome?.type

    var answer by useState<String>("")

    val doClientRegisterAnswer = { props.playerClient.registerAnswer(answer) }

    div {
        div {
            +"내 점수: ${score}점"
        }
        div {
            if (gameState == GameState.Finished) {
                +"퀴즈가 끝났습니다"
            } else {
                +"${quizNo}번 문제"
            }
        }

        when (gameState) {

            GameState.Ready -> {
                div {
                    +"문제를 기다려 주세요"
                }
            }

            GameState.Answering -> {
                div {
                    +"정답을 입력해 주세요"
                }
            }

            GameState.Answered -> {
                div {
                    +"정답을 확인해 주세요"
                }
            }

            GameState.Aggregated -> {
                div {

                    when (isCorrect) {

                        QuizOutcomeType.Correct -> {
                            +"정답입니다"
                        }

                        QuizOutcomeType.Incorrect -> {
                            +"오답입니다"
                        }

                        null -> {
                            +"-"
                        }
                    }
                }
            }

            GameState.Finished -> {
                div {
                    +"감사합니다!"
                }
            }
        }

        div {
            +"내 답안: ${props.registeredAnswer ?: ""}"
        }
        div {
            TextField {
                variant = FormControlVariant.filled
                onChange = { answer = it.target.asDynamic().value }
                onKeyUp = { if (it.key == "Enter") doClientRegisterAnswer() }
            }
        }
        div {
            Button {
                variant = ButtonVariant.contained
                onClick = { doClientRegisterAnswer() }
                +"정답 보내기"
            }
        }
    }
}


private external interface PlayerLoginComponentProps : Props {
    var playerClient: PlayerClient
    var enterFailed: Boolean
}

private val PlayerLoginComponent = FC<PlayerLoginComponentProps> { props ->

    var playerName by useState<String>("")

    val doClientEnter = { props.playerClient.enter(playerName) }

    div {
        div {
            +"Player Login"
        }
        div {
            if (props.enterFailed) {
                +"이름을 정확하게 입력해주세요"
            }
        }
        div {
            TextField {
                variant = FormControlVariant.filled
                onChange = { playerName = it.target.asDynamic().value }
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
