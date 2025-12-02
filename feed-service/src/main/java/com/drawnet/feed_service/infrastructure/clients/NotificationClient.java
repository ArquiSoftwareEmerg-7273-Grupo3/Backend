package com.drawnet.feed_service.infrastructure.clients;

import com.drawnet.feed_service.infrastructure.clients.dto.CreateNotificationFromCommentRequest;
import com.drawnet.feed_service.infrastructure.clients.dto.CreateNotificationFromReactionRequest;
import com.drawnet.feed_service.infrastructure.clients.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client para comunicarse con el Notification Service
 * Permite crear notificaciones cuando ocurren eventos en el Feed Service
 */
@FeignClient(
    name = "notification-service",
    path = "/api/v1/notifications"
)
public interface NotificationClient {
    
    /**
     * Crea una notificación cuando alguien comenta en un post
     * 
     * @param postAuthorId ID del autor del post (quien recibirá la notificación)
     * @param request Datos del comentario
     * @return Respuesta con la notificación creada
     */
    @PostMapping("/comments")
    ResponseEntity<NotificationResponse> createNotificationFromComment(
            @RequestParam("postAuthorId") Long postAuthorId,
            @RequestBody CreateNotificationFromCommentRequest request
    );
    
    /**
     * Crea una notificación cuando alguien reacciona a un post
     * 
     * @param postAuthorId ID del autor del post (quien recibirá la notificación)
     * @param request Datos de la reacción
     * @return Respuesta con la notificación creada
     */
    @PostMapping("/reactions")
    ResponseEntity<NotificationResponse> createNotificationFromReaction(
            @RequestParam("postAuthorId") Long postAuthorId,
            @RequestBody CreateNotificationFromReactionRequest request
    );
}
