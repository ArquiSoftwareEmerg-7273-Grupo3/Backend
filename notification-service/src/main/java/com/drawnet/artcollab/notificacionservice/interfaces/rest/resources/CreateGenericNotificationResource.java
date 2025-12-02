package com.drawnet.artcollab.notificacionservice.interfaces.rest.resources;

import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationPriority;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;

import java.time.LocalDateTime;

public record CreateGenericNotificationResource(
    Long recipientUserId,
    Long actorUserId,
    NotificationType type,
    String title,
    String message,
    NotificationPriority priority,
    String relatedEntityType,
    Long relatedEntityId,
    String actionUrl,
    String metadata,
    LocalDateTime expiresAt
) {
}
