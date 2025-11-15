package com.drawnet.artcollab.notificacionservice.domain.services;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;

import java.util.Optional;

public interface NotificationCommandService {
    Optional<Notification> handle(CreateNotificationFromCommentCommand command);

    Optional<Notification> handle(CreateNotificationFromReactionOnPostCommand command);

    Optional<Notification> handle(MarkNotificationAsReadCommand command);
}
