package com.drawnet.artcollab.notificacionservice.domain.services;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetAllNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationByIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationsByTypeQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationsByUserIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsCountQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    
    /**
     * Obtiene todas las notificaciones de un usuario
     */
    List<Notification> handle(GetAllNotificationsByUserQuery query);
    
    /**
     * Obtiene las notificaciones no leídas de un usuario
     */
    List<Notification> handle(GetUnreadNotificationsByUserQuery query);
    
    /**
     * Obtiene una notificación por ID
     */
    Optional<Notification> handle(GetNotificationByIdQuery query);
    
    /**
     * Obtiene las notificaciones de un usuario (paginadas)
     */
    Page<Notification> handle(GetNotificationsByUserIdQuery query);
    
    /**
     * Obtiene el conteo de notificaciones no leídas
     */
    Long handle(GetUnreadNotificationsCountQuery query);
    
    /**
     * Obtiene las notificaciones por tipo (paginadas)
     */
    Page<Notification> handle(GetNotificationsByTypeQuery query);
    
    /**
     * Obtiene las últimas notificaciones de un usuario
     */
    List<Notification> getRecentNotifications(Long userId);
}
