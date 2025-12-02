package com.drawnet.artcollab.notificacionservice.domain.model.commands;

public record DeleteNotificationCommand(Long notificationId, Long userId) {
}
