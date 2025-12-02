package com.drawnet.artcollab.notificacionservice.interfaces.rest.transform;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResource(Notification notification) {
        if (notification == null) return null;

        return new NotificationResource(
                notification.getId(),
                notification.getRecipientUserId(),
                notification.getActorUserId(),
                notification.getType() != null ? notification.getType().name() : null,
                notification.getTitle(),
                notification.getMessage(),
                notification.getPriority() != null ? notification.getPriority().name() : null,
                notification.getIsRead(),
                notification.getReadAt(),
                notification.getRelatedEntityId(),
                notification.getRelatedEntityType(),
                notification.getActionUrl(),
                notification.getCreatedAt(),
                notification.getExpiresAt(),
                notification.getActive()
        );
    }
}
