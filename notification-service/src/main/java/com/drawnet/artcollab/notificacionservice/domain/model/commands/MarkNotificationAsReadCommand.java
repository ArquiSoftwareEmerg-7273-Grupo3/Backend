package com.drawnet.artcollab.notificacionservice.domain.model.commands;

public record MarkNotificationAsReadCommand(
        Long notificationId,
        Long userId
) {}
