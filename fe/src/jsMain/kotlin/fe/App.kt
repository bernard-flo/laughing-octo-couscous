package fe

import fe.ext.stompjs.Client
import fe.ext.stompjs.PublishParams
import fe.ext.stompjs.StompConfig
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import shared.Greeting
import shared.HelloMessage

val App: FC<Props> = FC<Props> {

    Button {
        variant = ButtonVariant.contained
        onClick = { onClickCallback() }
        +"Button"
    }

}

private fun onClickCallback() {

    val stompClient = Client(
        StompConfig().apply {
            brokerURL = "ws://localhost:9080/stomp"
        }
    )

    stompClient.onConnect = {

        console.log("conntected")

        stompClient.subscribe("/user/topic/reply") { message ->

            val greeting = JSON.parse<Greeting>(message.body)
            console.log("/user/topic message")
            console.log(greeting)
        }

        stompClient.subscribe("/topic/reply") { message ->

            val greeting = JSON.parse<Greeting>(message.body)
            console.log("/topic message")
            console.log(greeting)
        }

        stompClient.publish(
            PublishParams(
                destination = "/app/hello",
                body = JSON.stringify(
                    HelloMessage(name = "John")
                )
            )
        )
    }

    stompClient.activate()
}
