@file:JsModule("@stomp/stompjs")

package fe.ext.stompjs

external class Client(
    conf: StompConfig?,
) {
    var onConnect: (IFrame) -> Unit
    var onStompError: (IFrame) -> Unit
    var onWebSocketError: (Any) -> Unit

    fun activate()
    fun publish(params: IPublishParams)
    fun subscribe(destination: String, callback: (IMessage) -> Unit)
}

external class StompConfig {
    var brokerURL: String?
}

external interface IPublishParams {
    var destination: String
    var body: String?
}


external interface IMessage : IFrame

external interface IFrame {
    var body: String
}
