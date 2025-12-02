package com.drawnet.artcollab.notificacionservice.interfaces.rest.resources;

import java.time.LocalDateTime;

public record NotificationResource(
        Long id,
        Long recipientUserId,
        Long actorUserId,
        String type,
        String title,
        String message,
        String priority,
        Boolean isRead,
        LocalDateTime readAt,
        Long relatedEntityId,
        String relatedEntityType,
        String actionUrl,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        Boolean active
) {
}
