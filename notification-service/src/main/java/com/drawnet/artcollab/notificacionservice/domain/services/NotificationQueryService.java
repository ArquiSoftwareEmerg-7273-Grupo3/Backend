package com.drawnet.artcollab.notificacionservice.domain.services;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetAllNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationByIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsByUserQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    List<Notification> handle(GetAllNotificationsByUserQuery query);

    List<Notification> handle(GetUnreadNotificationsByUserQuery query);

    Optional<Notification> handle(GetNotificationByIdQuery query);
}
