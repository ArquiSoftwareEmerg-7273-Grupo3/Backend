package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cliente Feign para comunicación con el servicio de chat/notificaciones
 */
@FeignClient(
    name = "chat-service", 
    path = "/api/v1",
    fallback = ChatServiceClientFallback.class
)
public interface ChatServiceClient {

    /**
     * Enviar notificación de nueva interacción en post
     */
    @PostMapping("/notifications/feed-interaction")
    void sendFeedInteractionNotification(@RequestBody FeedInteractionNotificationDto notification);

    /**
     * Obtener usuarios conectados (online)
     */
    @GetMapping("/users/online")
    List<String> getOnlineUsers();

    /**
     * Enviar notificación push
     */
    @PostMapping("/notifications/push")
    void sendPushNotification(@RequestBody PushNotificationDto notification);

    /**
     * Verificar si el usuario está online
     */
    @GetMapping("/users/{userId}/online")
    boolean isUserOnline(@PathVariable("userId") String userId);

    /**
     * DTO para notificaciones de interacciones del feed
     */
    record FeedInteractionNotificationDto(
        String recipientUserId,
        String actorUserId,
        String postId,
        NotificationType type,
        String message,
        LocalDateTime timestamp
    ) {}

    /**
     * DTO para notificaciones push
     */
    record PushNotificationDto(
        String userId,
        String title,
        String message,
        String action,
        String postId
    ) {}

    /**
     * Tipos de notificaciones
     */
    enum NotificationType {
        POST_LIKE,
        POST_COMMENT,
        POST_REPOST,
        COMMENT_REPLY,
        COMMENT_LIKE,
        FOLLOW
    }
}