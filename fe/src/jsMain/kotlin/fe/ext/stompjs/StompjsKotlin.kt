package fe.ext.stompjs

data class PublishParams(
    override var destination: String,
    override var body: String?,
) : IPublishParams
