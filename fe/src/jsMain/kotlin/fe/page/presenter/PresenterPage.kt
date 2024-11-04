package fe.page.presenter

import fe.client.presenter.PresenterClient
import mui.icons.material.StarOutline
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Card
import mui.material.FormControlVariant
import mui.material.PaperVariant
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
import shared.domain.common.ChatItem
import shared.domain.game.Leaderboard
import web.cssom.BoxSizing
import web.cssom.Color
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.JustifyContent
import web.cssom.Overflow
import web.cssom.pct
import web.cssom.px
import web.cssom.vh
import web.html.InputType

internal val PresenterPage = FC<Props> {

    var entered by useState(false)
    val (enterFailed, setEnterFailed) = useState(false)
    var leaderboard by useState<Leaderboard>(listOf())
    var chatList by useState<List<ChatItem>>(listOf())

    val presenterClientRef = {
        val cachedChatList = mutableListOf<ChatItem>()
        useRef(
            PresenterClient(
                onEntered = {
                    entered = true
                },
                onEnterFailed = {
                    setEnterFailed(true)
                },
                onLeaderboardUpdated = {
                    leaderboard = it
                },
                onChatUpdated = {
                    cachedChatList.add(it.chatItem)
                    if (cachedChatList.size > 20) {
                        cachedChatList.removeFirst()
                    }
                    chatList = cachedChatList.toList()
                },
            )
        )
    }()

    div {
        if (entered) {
            PresenterGameComponent {
                this.leaderboard = leaderboard
                this.chatList = chatList
            }
        } else {
            PresenterLoginComponent {
                this.presenterClient = presenterClientRef.current!!
                this.enterFailed = enterFailed
                this.setEnterFailed = setEnterFailed
            }
        }
    }

}


private external interface PresenterGameComponentProps : Props {
    var leaderboard: Leaderboard
    var chatList: List<ChatItem>
}

private val PresenterGameComponent = FC<PresenterGameComponentProps> { props ->

    Box {
        sx {
            height = 95.vh
        }

        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column
                boxSizing = BoxSizing.borderBox
                height = 70.pct
                padding = 30.px
                gap = 30.px
                overflow = Overflow.scroll
                scrollbarColor = "#ffffff #ffffff".asDynamic()
            }

            for (leaderboardItem in props.leaderboard) {
                val rankValue = leaderboardItem.rank.value
                val playerNameValue = leaderboardItem.playerName.value
                val scoreValue = leaderboardItem.score.value

                Box {
                    sx {
                        display = Display.flex
                    }

                    if (rankValue == 1) {
                        StarOutline {
                            sx {
                                fontSize = 50.px
                            }
                        }
                    }

                    Typography {
                        variant = TypographyVariant.h3

                        +"${rankValue}등 ${playerNameValue} (${scoreValue}점)"
                    }
                }
            }
        }

        Box {
            sx {
                height = 30.pct
            }
            Card {
                sx {
                    display = Display.flex
                    flexDirection = FlexDirection.columnReverse
                    boxSizing = BoxSizing.borderBox
                    width = 100.pct
                    height = 100.pct
                    marginTop = 20.px
                    padding = 20.px
                    backgroundColor = Color("#fafafa")
                    overflow = Overflow.hidden
                }
                variant = PaperVariant.outlined

                for (chatItem in props.chatList.reversed()) {
                    val playerNameValue = chatItem.playerName.value
                    val messageValue = chatItem.chatMessage.value

                    Typography {
                        variant = TypographyVariant.h6

                        +"${playerNameValue}: ${messageValue}"
                    }
                }
            }
        }
    }
}


private external interface PresenterLoginComponentProps : Props {
    var presenterClient: PresenterClient
    var enterFailed: Boolean
    var setEnterFailed: StateSetter<Boolean>
}

private val PresenterLoginComponent = FC<PresenterLoginComponentProps> { props ->

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
