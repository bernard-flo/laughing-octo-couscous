package fe.page.player

import fe.client.player.PlayerClient
import fe.ext.notistack.SnackbarProvider
import fe.ext.notistack.enqueueSnackbar
import mui.icons.material.ArrowCircleRight
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Dialog
import mui.material.DialogActions
import mui.material.DialogContent
import mui.material.DialogContentText
import mui.material.FormControlVariant
import mui.material.IconButton
import mui.material.InputBase
import mui.material.Paper
import mui.material.TextField
import mui.material.Typography
import mui.material.TypographyAlign
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.StateSetter
import react.dom.html.ReactHTML.div
import react.dom.onChange
import react.useEffect
import react.useRef
import react.useState
import shared.domain.game.GameState
import shared.domain.game.GameStateInfo
import shared.domain.game.PlayerQuizOutcome
import shared.domain.game.QuizOutcomeType
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.JustifyContent
import web.cssom.pct
import web.cssom.px

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
                if (it.gameStateInfo.gameState == GameState.Ready) {
                    registeredAnswer = ""
                }
            },
            onAnswerRegistered = {
                registeredAnswer = it.registeredAnswer.value
            },
            onRegisterAnswerFailed = {
                enqueueSnackbar("잠시 후에 입력해주세요")
            },
            onQuizOutcome = {
                quizOutcome = it
            },
        )
    )

    useEffect(null) { playerClientRef.current!!.tryEnter() }

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
    val quizOutcome = props.quizOutcome

    var answer by useState<String>("")
    var chatMessage by useState<String>("")
    var isChatDialogOpen by useState<Boolean>(false)

    val doClientRegisterAnswer = {
        if (answer.isNotBlank()) {
            props.playerClient.registerAnswer(answer)
            answer = ""
        }
    }
    val doClientChat = {
        if (chatMessage.isNotBlank()) {
            props.playerClient.chat(chatMessage)
            chatMessage = ""
        }
    }

    SnackbarProvider {
        anchorOrigin = js("{ horizontal: 'left', vertical: 'top' }")
        autoHideDuration = 1500
        maxSnack = 2
        variant = "error"
    }

    Box {
        sx {
            display = Display.flex
            justifyContent = JustifyContent.center
        }
        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column
                width = 70.pct
                marginTop = 40.px
                maxWidth = 500.px
                gap = 17.px
            }
            Box {
                sx {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.right
                    width = 100.pct
                }
                Typography {
                    variant = TypographyVariant.h6
                    +"현재 점수: ${score}점"
                }
            }
            Typography {
                variant = TypographyVariant.h4
                align = TypographyAlign.center

                if (gameState == GameState.Finished) {
                    +"퀴즈가 끝났습니다"
                } else {
                    +"${quizNo}번 문제"
                }
            }
            Typography {
                variant = TypographyVariant.h6
                align = TypographyAlign.center

                when (gameState) {
                    GameState.Ready -> {
                        +"문제를 기다려 주세요"
                    }

                    GameState.Answering -> {
                        +"정답을 입력해 주세요"
                    }

                    GameState.Answered -> {
                        +"정답을 확인해 주세요"
                    }

                    GameState.Aggregated -> {
                        when (quizOutcome?.type) {

                            QuizOutcomeType.Early -> {
                                +"정답입니다! 선착순 점수 추가! (${quizOutcome.point.value}점)"
                            }

                            QuizOutcomeType.Correct -> {
                                +"정답입니다! (${quizOutcome.point.value}점)"
                            }

                            QuizOutcomeType.Partial -> {
                                +"부분 정답입니다! (${quizOutcome.point.value}점)"
                            }

                            QuizOutcomeType.Incorrect -> {
                                +"오답입니다."
                                if (quizOutcome.point.value != 0) {
                                    +" (${quizOutcome.point.value}점)"
                                }
                            }

                            null -> {
                                +"-"
                            }
                        }
                    }

                    GameState.Finished -> {
                        +"감사합니다!"
                    }
                }
            }
            Typography {
                variant = TypographyVariant.subtitle1
                +"내가 보낸 답안: ${props.registeredAnswer ?: ""}"
            }
            Paper {
                sx {
                    display = Display.flex
                    height = 50.px
                    marginTop = (-10).px
                    paddingLeft = 10.px
                }
                InputBase {
                    fullWidth = true
                    placeholder = "답안 입력"
                    value = answer
                    onChange = { answer = it.target.asDynamic().value }
                    onKeyUp = { if (it.key == "Enter") doClientRegisterAnswer() }
                }
                IconButton {
                    ArrowCircleRight()
                    onClick = { doClientRegisterAnswer() }
                }
            }
            Button {
                sx {
                    marginTop = 20.px
                }
                fullWidth = true
                variant = ButtonVariant.outlined
                onClick = { isChatDialogOpen = true }
                +"채팅 입력"
            }
            Dialog {
                fullWidth = true
                maxWidth = false
                open = isChatDialogOpen
                onKeyDown = { if (it.key == "Escape") isChatDialogOpen = false }
                DialogContent {
                    DialogContentText {
                        Paper {
                            sx {
                                display = Display.flex
                                paddingLeft = 10.px
                                height = 50.px
                            }
                            InputBase {
                                fullWidth = true
                                placeholder = "채팅 입력"
                                value = chatMessage
                                onChange = { chatMessage = it.target.asDynamic().value }
                                onKeyUp = { if (it.key == "Enter") doClientChat() }
                            }
                            IconButton {
                                ArrowCircleRight()
                                onClick = { doClientChat() }
                            }
                        }
                    }
                    DialogActions {
                        Button {
                            onClick = { isChatDialogOpen = false }
                            +"닫기"
                        }
                    }
                }
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
            marginTop = 100.px
            gap = 40.px
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
