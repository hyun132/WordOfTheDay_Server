package com.hyun.demo.configuration

import com.hyun.demo.util.AuthHandshakeInterceptor
import com.hyun.demo.util.SttWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val sttWebSocketHandler: SttWebSocketHandler,
    private val authHandshakeInterceptor: AuthHandshakeInterceptor
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(sttWebSocketHandler, "/ws/stt")
            .addInterceptors(authHandshakeInterceptor)
            .setAllowedOrigins("*")
    }
}