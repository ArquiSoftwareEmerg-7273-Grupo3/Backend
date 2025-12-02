package com.drawnet.artcollab.notificacionservice.domain.services;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.DeleteNotificationCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkAllAsReadCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;

import java.util.Optional;

public interface NotificationCommandService {
    
    /**
     * Crea una nueva notificación
     */
    Optional<Notification> handle(CreateNotificationCommand command);
    
    /**
     * Crea una notificación desde un comentario
     */
    Optional<Notification> handle(CreateNotificationFromCommentCommand command);
    
    /**
     * Crea una notificación desde una reacción en un post
     */
    Optional<Notification> handle(CreateNotificationFromReactionOnPostCommand command);
    
    /**
     * Marca una notificación como leída
     */
    Optional<Notification> handle(MarkNotificationAsReadCommand command);
    
    /**
     * Marca todas las notificaciones de un usuario como leídas
     */
    int handle(MarkAllAsReadCommand command);
    
    /**
     * Elimina (desactiva) una notificación
     */
    boolean handle(DeleteNotificationCommand command);
}
