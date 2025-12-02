package com.drawnet.feed_service.infrastructure.clients.dto;

/**
 * DTO para solicitar la creación de una notificación desde una reacción
 */
public record CreateNotificationFromReactionRequest(
    Long postId,
    Long reactorId,
    String reactionType
) {}
