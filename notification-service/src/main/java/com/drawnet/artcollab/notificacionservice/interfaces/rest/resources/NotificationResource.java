package com.drawnet.artcollab.notificacionservice.interfaces.rest.resources;

import java.time.LocalDateTime;

public record NotificationResource(
        Long id,
        Long recipientId,
        Long actorId,
        Long postId,
        Long commentId,
        String type,        // NotificationType como texto
        String sourceType,  // NotificationSourceType como texto
        String status,      // NotificationStatus como texto
        String message,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {
}
