package com.drawnet.artcollab.notificacionservice.interfaces.rest.transform;

import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.MarkNotificationAsReadResource;

public class MarkNotificationAsReadCommandFromResourceAssembler {
    public static MarkNotificationAsReadCommand toCommand(
            Long notificationId,
            MarkNotificationAsReadResource resource
    ) {
        return new MarkNotificationAsReadCommand(
                notificationId,
                resource.userId()
        );
    }
}
