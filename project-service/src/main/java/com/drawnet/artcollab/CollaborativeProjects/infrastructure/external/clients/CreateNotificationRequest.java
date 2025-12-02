package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import java.time.LocalDateTime;

public record CreateNotificationRequest(
    Long recipientUserId,
    Long actorUserId,
    String type,  // Will be converted to NotificationType enum by the notification service
    String title,
    String message,
    String priority,  // Will be converted to NotificationPriority enum by the notification service
    String relatedEntityType,
    Long relatedEntityId,
    String actionUrl,
    String metadata,
    LocalDateTime expiresAt
) {
    // Constructor sin expiresAt para compatibilidad
    public CreateNotificationRequest(Long recipientUserId, Long actorUserId, String type, 
                                    String title, String message, String priority,
                                    String relatedEntityType, Long relatedEntityId, 
                                    String actionUrl, String metadata) {
        this(recipientUserId, actorUserId, type, title, message, priority, 
             relatedEntityType, relatedEntityId, actionUrl, metadata, null);
    }
}
