package fe

import fe.client.player.PlayerClient
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
import react.dom.html.ReactHTML.div
import react.dom.onChange
import react.useRef
import react.useState
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.PlayerQuizOutcome
import shared.domain.game.QuizOutcomeType
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.rem

internal val PlayerPage = FC<Props> {

    var entered by useState(false)
    val (enterFailed, setEnterFailed) = useState(false)
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
                setEnterFailed(true)
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
                this.setEnterFailed = setEnterFailed
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
    var chatMessage by useState<String>("")

    val doClientRegisterAnswer = { props.playerClient.registerAnswer(answer) }
    val doClientChat = {
        if (chatMessage.isNotBlank()) {
            props.playerClient.chat(chatMessage)
            chatMessage = ""
        }
    }

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
        div {
            TextField {
                variant = FormControlVariant.filled
                value = chatMessage
                onChange = { chatMessage = it.target.asDynamic().value }
                onKeyUp = { if (it.key == "Enter") doClientChat() }
            }
        }
        div {
            Button {
                variant = ButtonVariant.contained
                onClick = { doClientChat() }
                +"채팅"
            }
        }
    }
}


private external interface PlayerLoginComponentProps : Props {
    var playerClient: PlayerClient
    var enterFailed: Boolean
    var setEnterFailed: StateSetter<Boolean>
}

private val PlayerLoginComponent = FC<PlayerLoginComponentProps> { props ->

    var playerName by useState<String>("")

    val doClientEnter = { props.playerClient.enter(playerName) }

    Box {
        sx {
            display = Display.grid
            justifyContent = JustifyContent.center
            marginTop = 5.rem
            gap = 2.rem
        }

        Typography {
            variant = TypographyVariant.h2
            +"Townhall"
        }
        TextField {
            variant = FormControlVariant.outlined
            label = ReactNode("영어 이름")
            error = props.enterFailed
            helperText = if (props.enterFailed) ReactNode("이름을 정확하게 입력해주세요") else null
            onChange = {
                playerName = it.target.asDynamic().value
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
