package com.drawnet.artcollab.notificacionservice.domain.model.queries;

import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;

public record GetNotificationsByTypeQuery(Long userId, NotificationType type, int page, int size) {
}
