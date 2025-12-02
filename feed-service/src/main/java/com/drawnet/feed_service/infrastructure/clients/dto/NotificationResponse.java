package com.drawnet.feed_service.infrastructure.clients.dto;

import java.time.LocalDateTime;

/**
 * DTO de respuesta del Notification Service
 */
public record NotificationResponse(
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
) {}
