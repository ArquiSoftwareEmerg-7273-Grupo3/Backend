package com.drawnet.feed_service.infrastructure.clients.dto;

/**
 * DTO para solicitar la creación de una notificación desde un comentario
 */
public record CreateNotificationFromCommentRequest(
    Long commentId,
    Long postId,
    Long commenterId,
    String commentContent
) {}
