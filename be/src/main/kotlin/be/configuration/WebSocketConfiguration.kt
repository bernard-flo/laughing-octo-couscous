package be.configuration

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration
import org.springframework.web.socket.handler.WebSocketHandlerDecorator


@EnableWebSocketMessageBroker
@Configuration
private class WebSocketConfiguration(
    private val applicationEventPublisher: ApplicationEventPublisher,
) : WebSocketMessageBrokerConfigurer {

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {

        registry.addDecoratorFactory {
            object : WebSocketHandlerDecorator(it) {

                override fun afterConnectionEstablished(session: WebSocketSession) {
                    super.afterConnectionEstablished(session)
                    applicationEventPublisher.publishEvent(WebSocketEstablishedEvent(this, session.id))
                }

                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                    applicationEventPublisher.publishEvent(WebSocketClosedEvent(this, session.id))
                    super.afterConnectionClosed(session, closeStatus)
                }
            }
        }
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {

        registry.addEndpoint("/stomp").setAllowedOrigins("*")
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {

        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {

        registration.interceptors(object : ChannelInterceptor {

            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {

                val stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
                checkNotNull(stompHeaderAccessor)

                if (stompHeaderAccessor.command == StompCommand.SEND) {
                    val destination = stompHeaderAccessor.destination
                        ?: return null
                    if (destination.startsWith("/app") == false) {
                        return null
                    }
                }

                return message
            }
        })
    }

}


class WebSocketEstablishedEvent(
    source: Any,
    val sessionId: String,
) : ApplicationEvent(source)

class WebSocketClosedEvent(
    source: Any,
    val sessionId: String,
) : ApplicationEvent(source)
