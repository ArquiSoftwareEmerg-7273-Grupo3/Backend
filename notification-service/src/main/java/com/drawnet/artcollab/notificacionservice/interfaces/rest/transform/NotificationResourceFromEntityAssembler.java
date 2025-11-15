package com.drawnet.artcollab.notificacionservice.interfaces.rest.transform;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResource(Notification notification) {
        if (notification == null) return null;

        return new NotificationResource(
                notification.getId(),
                notification.getRecipientId(),
                notification.getActorId(),
                notification.getPostId(),
                notification.getCommentId(),
                notification.getType() != null ? notification.getType().name() : null,
                notification.getSourceType() != null ? notification.getSourceType().name() : null,
                notification.getStatus() != null ? notification.getStatus().name() : null,
                notification.getMessage(),
                notification.isActive(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}
