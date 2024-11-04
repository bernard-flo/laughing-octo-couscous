package fe.page.presenter

import fe.client.presenter.PresenterClient
import mui.icons.material.StarOutline
import mui.material.Box
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useRef
import react.useState
import shared.domain.game.GroupLeaderboard
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.px

internal val PresenterMorePage = FC<Props> {

    var entered by useState(false)
    val (enterFailed, setEnterFailed) = useState(false)
    var groupLeaderboard by useState<GroupLeaderboard>(listOf())

    val presenterClientRef = {
        useRef(
            PresenterClient(
                onEntered = {
                    entered = true
                },
                onEnterFailed = {
                    setEnterFailed(true)
                },
                onLeaderboardUpdated = {},
                onGroupLeaderboardUpdated = {
                    groupLeaderboard = it
                },
                onChatUpdated = {},
            )
        )
    }()

    div {
        if (entered) {
            PresenterMoreGameComponent {
                this.groupLeaderboard = groupLeaderboard
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


private external interface PresenterMoreGameComponentProps : Props {
    var groupLeaderboard: GroupLeaderboard
}

private val PresenterMoreGameComponent = FC<PresenterMoreGameComponentProps> { props ->

    Box {
        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
        }

        for (groupItem in props.groupLeaderboard) {
            val rankValue = groupItem.rank.value
            val groupValue = groupItem.group.value
            val groupScoreString = groupItem.groupScore.value.asDynamic().toFixed(2) as String

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

                    +"${rankValue}등 ${groupValue} (${groupScoreString}점)"
                }
            }


            Typography {
                variant = TypographyVariant.h6

                for (playerItem in groupItem.playerItemList) {
                    val playerNameValue = playerItem.playerName.value
                    val playerScoreValue = playerItem.score.value

                    +"${playerNameValue}(${playerScoreValue}점)"
                }
            }
        }
    }
}
