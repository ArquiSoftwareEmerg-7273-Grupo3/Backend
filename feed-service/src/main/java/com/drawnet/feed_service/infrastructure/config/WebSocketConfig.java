package com.drawnet.feed_service.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Configuración de WebSocket con STOMP para comunicación en tiempo real
 * 
 * STOMP (Simple Text Oriented Messaging Protocol) es compatible con:
 * - @stomp/stompjs en el frontend
 * - SockJS como capa de transporte
 * - Topics para pub/sub pattern
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configurar el Message Broker
     * 
     * - SimpleBroker: Maneja mensajes del servidor hacia los clientes
     * - ApplicationDestinationPrefixes: Prefijo para mensajes del cliente al servidor
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar broker simple para topics
        // El frontend se suscribe a: /topic/post-created, /topic/comment-created, etc.
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefijo para mensajes desde el cliente (opcional, para comunicación bidireccional)
        // Ejemplo: cliente envía a /app/message, servidor recibe en @MessageMapping("/message")
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registrar endpoint STOMP
     * 
     * El frontend se conecta a este endpoint usando SockJS
     * URL completa: http://localhost:8080/ws (a través del API Gateway)
     * URL directa: http://localhost:8087/ws (directo al feed-service)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // Permitir conexiones desde el frontend Angular
                .setAllowedOriginPatterns("*")
                // SockJS: Fallback para navegadores sin soporte nativo WebSocket
                .withSockJS();
    }
}
