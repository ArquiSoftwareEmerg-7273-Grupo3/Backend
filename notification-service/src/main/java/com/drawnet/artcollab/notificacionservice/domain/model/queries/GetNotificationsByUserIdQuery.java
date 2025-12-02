package com.drawnet.artcollab.notificacionservice.domain.model.queries;

public record GetNotificationsByUserIdQuery(Long userId, int page, int size) {
}
